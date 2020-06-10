package com.example.demo.selenium;

import com.alibaba.fastjson.JSON;
import com.example.demo.selenium.bean.CommandStep;
import com.example.demo.selenium.bean.Record;
import com.example.demo.selenium.bean.SeleniumTest;
import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;
import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableMap;
import jodd.template.StringTemplateParser;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import javax.annotation.Nonnull;
import javax.swing.text.StyledEditorKit;
import java.util.function.Supplier.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.NoSuchElementException;

import static com.google.common.base.Preconditions.*;

/**
 * Selenium 录像执行器
 * <p>
 * 回放SeleniumIDE的录像。
 * <p>
 * note: 1、当前为顺序回放各用例内的命令 2、不执行所有的流程控制命令（条件分支、循环等）
 * <p>
 * 命令的执行：由于命令接近100个，switch语句不便于维护，因此使用反射查找命令的形式执行命令。
 * 执行命令时，执行'emit+command'(首字母大写)的方法
 *
 * @author miaoshaodong
 * @date Creater in 11:42 2019/11/13
 */
@Slf4j
public class RecordRunner {
    static final Map<String, Method> REGISTRY;
    private static final String VALUE = "value";
    private static final String MSG_SKIP_FLOW = "跳过控制流 {}";
    /**
     * 字符串模板
     */
    private static final StringTemplateParser TEMPLATE_PARSER = new StringTemplateParser().setReplaceMissingKey(false);

    static {
        REGISTRY = gatherRegistry();
    }

    /**
     * 浏览器驱动
     */
    private final ChromeDriver driver;
    /**
     * 浏览器等待对象（当前为定长时间）
     */
    private final WebDriverWait driverWait;
    /**
     * 录像
     */
    private final Record record;
    /**
     * 当前命令
     * <p>
     * 便于命令分发和生成执行日志
     */
    private CommandStep current;
    /**
     * 变量存储
     * <p>
     * 跨命令的变量存储与该Map
     */
    private Map<String, Object> vars;

    /**
     * 构造方法
     *
     * @param driver 浏览器对象
     * @param record 录像
     */
    public RecordRunner(@NonNull ChromeDriver driver, @Nonnull Record record) {
        this.driver = driver;
        this.record = record;
        driverWait = new WebDriverWait(driver, 15);
    }

    /**
     * 收集注册的命令
     * 获取command到Method的映射：方法名为'emit+command'(首字母大写)，如'click'映射的方法名为'emitClick'
     *
     * @return 命令到方法的映射
     */
    private static Map<String, Method> gatherRegistry() {
        ImmutableMap.Builder<String, Method> builder = ImmutableMap.builder();
        for (Method method : RecordRunner.class.getDeclaredMethods()) {
            String name = method.getName();
            if (name.startsWith("emit")) {
                String command = Character.toLowerCase(name.charAt(4)) + name.substring(5);
                builder.put(command, method);
            }
        }
        return builder.build();
    }

    public CommandStep getCurrent() {
        return current;
    }

    public Object getVar(String name) {
        if (vars == null) {
            return null;
        }
        return vars.get(name);
    }

    private String getVarStr(String name) {
        Object o = getVar(name);
        if (o == null) {
            return null;
        }
        return String.valueOf(o);
    }

    /**
     * 字符串模板解析
     *
     * @param str 字符串模板（包含'${变量名}'）
     * @return 替换后的文本
     */
    private String prepareStr(String str) {
        if (Strings.isNullOrEmpty(str) || !str.contains(StringTemplateParser.DEFAULT_MACRO_START)) {
            return str;
        }
        return TEMPLATE_PARSER.parse(str, this::prepareStr);
    }

    private void putVar(String name, Object object) {
        if (object == null) {
            return;
        }
        checkNotNull(name, "变量名称不能为null");
        if (vars == null) {
            vars = new LinkedHashMap<>();
        }
        vars.put(name, object);
    }

    private JavascriptExecutor js() {
        return driver;
    }

    /**
     * 执行录像
     * <p>
     * 命令出错时，将抛出异常，可以通过current获取当前的命令
     */
    public void run() throws InterruptedException {
        List<SeleniumTest> tests = record.getTests();
        if (tests == null || tests.isEmpty()) {
            return;
        }
        for (SeleniumTest test : tests) {
            List<CommandStep> commands = test.getCommands();
            if (commands == null || commands.isEmpty()) {
                continue;
            }

            for (CommandStep command : commands) {
                runCommand(command);
            }
            //printWebLog(driver);
        }
        current = null;
    }

    public static void printWebLog(ChromeDriver driver) throws InterruptedException {
        LogEntries logEntries =driver.manage().logs().get(LogType.BROWSER);
        for(LogEntry entry : logEntries) {
            //依次打印出console信息
            log.info("chrome.console==== {}",entry.getMessage());
        }
    }
    private void runCommand(CommandStep command) {
        current = command;
        String stepCommand = command.getCommand();
        if (Strings.isNullOrEmpty(stepCommand) || stepCommand.startsWith(Constants.DOUBLE_SLASH)) {
            return;
        }
        Method method = REGISTRY.get(stepCommand);
        checkNotNull(method, "不支持的命令%", stepCommand);
        long start = System.currentTimeMillis();
        try {
            log.debug("执行：{}", Constants.str(command));
            Boolean opensWindow = command.getOpensWindow();
            if (opensWindow == null || !opensWindow) {
                method.invoke(this);
            } else {
                waitForWindow(command, method);
            }
        } catch (IllegalAccessException e) {
            log.error("代码编写错误：无法调用方法 {}", Constants.str(command), e);
        } catch (InvocationTargetException e) {
            log.info("页面异常当前系统不需要处理");
            //throwException(e);
        } finally {
            log.debug("耗时 {}", System.currentTimeMillis() - start);
        }

    }

    private void waitForWindow(CommandStep command, Method method)
            throws InvocationTargetException, IllegalAccessException {
        String windowHandleName = checkNotNull(command.getWindowHandleName(), "命令格式错误：未指定windowHandleName");
        //记录窗口列表
        Set<String> whThen = driver.getWindowHandles();
        method.invoke(this);
        //比对当前窗口列表，获取到新开窗口
        driverWait.until(d -> {
            Set<String> whNow = driver.getWindowHandles();
            return (whNow.size() > whThen.size());
        });
        Set<String> whNow = driver.getWindowHandles();
        whNow.remove(whThen);
        String newWin = whNow.iterator().next();
        putVar(windowHandleName, newWin);
    }

    /*private void throwException(InvocationTargetException e) {
        Throwable cause = e.getCause();
        if (cause == null) {
            //不可能出现
            throw new IllegalStateException("执行失败" + e.getMessage(), e);
        }
        while (cause != null) {
            if (cause instanceof NoSuchElementException) {
                throw new IllegalStateException("页面元素找不到", cause);
            }
            cause = cause.getCause();
        }
        cause = e.getCause();
        if (cause instanceof TimeoutException) {
           log.warn("有异常",cause);
           throw new IllegalStateException("执行超时", cause);
        }
        if (cause instanceof ElementClickInterceptedException) {
            throw new IllegalStateException("页面元素不可点击", cause);
        }
        if (cause instanceof UnhandledAlertException) {
            String alertText = ((UnhandledAlertException) cause).getAlertText();
            throw new IllegalStateException("弹出了警告框" + alertText);
        }
        //出错啦，步骤执行失败
        throw new IllegalStateException("执行失败" + Constants.trimMessage(cause), cause);
    }*/

    /**
     * add selection
     */
    public void emitAddSelection() {
        emitSelect();
    }

    /**
     * select
     */
    private void emitSelect() {
        WebElement dropDown = findElement(current.getTarget());
        By by = Constants.parseSelection(current.getValue());
        dropDown.findElement(by).click();
    }

    /**
     * ansewer on next prompt
     */
    public void emitAnswerOnNextPrompt() {
        Alert alert = driver.switchTo().alert();
        alert.sendKeys(Constants.parseKeys(current.getValue()));
        alert.accept();
    }

    /**
     * assert
     */
    public void emitAssert() {
        assertText(() -> String.valueOf(getVar(current.getTarget())));
    }

    /**
     * assert alert
     */
    public void emitAssertAlert() {
        assertText(() -> driver.switchTo().alert().getText());
    }

    /**
     * assert checked
     */
    public void emitAssertChecked() {
        boolean selected = findElement(current.getTarget()).isSelected();
        checkState(selected);
    }

    /**
     * assert confirmation
     */
    public void emitAssertConfirmation() {
        emitAssertAlert();
    }

    /**
     * assert editable
     */
    public void emitAssertEditable() {
        WebElement element = findElement(current.getTarget());
        boolean isEditable = element.isEnabled() && element.getAttribute("readonly") == null;
        checkState(isEditable);
    }

    /**
     * assert element present
     */
    public void emitAssertElementPresent() {
        List<WebElement> elements = driver.findElements(Constants.parseBy(current.getTarget()));
        checkState(!elements.isEmpty());
    }

    /**
     * assert element not present
     */
    public void emitAssertElementNotPresent() {
        List<WebElement> elements = driver.findElements(Constants.parseBy(current.getTarget()));
        checkState(elements.isEmpty());
    }

    /**
     * assert not editable
     */
    public void emitAssertNotEditable() {
        WebElement element = findElement(current.getTarget());
        boolean isEditable = element.isEnabled() && element.getAttribute("readonly") == null;
        checkState(!isEditable);
    }

    /**
     * assert not checked
     */
    private void emitAssertNotChecked() {
        WebElement element = findElement(current.getTarget());
        checkState(!element.isSelected());
    }

    /**
     * assert not selected value
     */
    public void emitAssertNotSelectedValue() {
        assertText(() -> findElement(current.getTarget()).getAttribute(VALUE));
    }

    /**
     * assert not text
     */
    public void emitAssertNotText() {
        assertNotText(()-> findElement(current.getTarget()).getText());
    }

    private void assertNotText(Supplier<String> supplier) {
        String wanted = current.getValue();
        checkArgument(wanted != null, "规则错误：未指定value");
        String text = supplier.get();
        if (wanted.equals(text)) {
            throw new IllegalStateException("预期内容为：[" + wanted + "],但是内容为[" + text + "]");
        }
    }

    /**
     * assert prompt
     */
    public void emitAssertPrompt() {
        assertText(() -> {
            return driver.switchTo().alert().getText();
        });
    }

    /**
     * assert selected value
     */
    public void emitAssertSelectedValue() {
        assertText(() -> {
            return findElement(current.getTarget()).getAttribute(VALUE);
        });
    }

    /**
     * assert selected label
     */
    public void emitAssertSelectedLabel() {
        assertText(() -> {
                    WebElement element = findElement(current.getTarget());
                    String value = element.getAttribute(VALUE);
                    String locator = String.format("option[@value = '%s']", value);
                    return element.findElement(By.xpath((locator))).getText();
                }
        );
    }

    /**
     * assert text
     */
    public void emitAssertText() {
        assertText(() -> {
            return findElement(current.getTarget()).getText();
        });
    }

    /**
     * assert title
     */
    public void emitAssertTitle() {
        assertText(driver::getTitle);
    }

    /**
     * assert value
     */
    public void emitAssertValue() {
        assertText(() -> findElement(current.getTarget()).getAttribute(VALUE));
    }

    /**
     * check
     */
    public void emitCheck() {
        WebElement element = findElement(current.getTarget());
        if (!element.isSelected()) {
            element.click();
        }
    }

    /**
     * choose cancel on Next confirmation
     */
    public void emitChooseCancelOnNextConfirmation() {
        driver.switchTo().alert().dismiss();
    }

    /**
     * choose cancel on text prompt
     */
    public void emitChooseCancelOnNextPrompt() {
        driver.switchTo().alert().dismiss();
    }

    /**
     * choose ok on Next confirmation
     */
    public void emitChooseOkOnNextConfirmation() {
        driver.switchTo().alert().dismiss();
    }

    /**
     * click
     */
    public void emitClick() {
        Date startTime = new Date();
        By by = Constants.parseBy(current.getTarget());
        WebElement element = driverWait.until(ExpectedConditions.elementToBeClickable(by));
        try {
            element.click();
        } catch (ElementClickInterceptedException e) {
            log.info("click fail,trying js click, {}", new Constants.TrimLog(e));
            js().executeScript("arguments[0].click();", element);
        }
    }

    /**
     * click at
     */
    public void emitClickAt() {
        emitClick();
    }

    /**
     * close
     */
    public void emitClose() {
        driver.close();
    }

    /**
     * debugger
     */
    public void emitDebugger() {
        log.warn("Selenium无实现 {}", Constants.str(current));
    }

    /**
     * do
     */
    public void emitDo() {
        log.warn(MSG_SKIP_FLOW, Constants.str(current));
    }

    /**
     * double click
     */
    public void emitDoubleClick() {
        WebElement element = findElement(current.getTarget());
        new Actions(driver).doubleClick(element).perform();
    }

    /**
     * double click at
     */
    public void emitDoubleClickAt() {
        emitClick();
    }

    /**
     * drag and drop to object
     */
    public void emitDragAndDropToObject() {
        WebElement dragged = findElement(current.getTarget());
        WebElement dropped = findElement(current.getValue());
        new Actions(driver).dragAndDrop(dragged, dropped).perform();
    }

    /**
     * echo
     */
    public void emitEcho() {
        String message = MoreObjects.firstNonNull(current.getTarget(), current.getValue());
        log.info("echo: {}", prepareStr(message));
    }

    /**
     * edit content
     */
    public void emitEditContent() {
        WebElement element = findElement(current.getTarget());
        js().executeScript(
                "if(argument[0].contentEditable === 'true') {argument[0].innertext = argument[1]}",
                element, current.getValue()
        );
    }

    /**
     * else
     */
    public void emitElse() {
        log.warn(MSG_SKIP_FLOW, Constants.str(current));
    }

    /**
     * else if
     */
    public void emitElseIf() {
        log.warn(MSG_SKIP_FLOW, Constants.str(current));
    }

    /**
     * end
     */
    public void emitEnd() {
        log.warn(MSG_SKIP_FLOW, Constants.str(current));
    }

    /**
     * execute Script
     */
    public void emitExecuteScript() {
        Object result = js().executeScript(current.getTarget());
        String varName = current.getValue();
        if (!Strings.isNullOrEmpty(varName)) {
            putVar(varName, result);

        }
    }

    /**
     * execute async Script
     */
    public void emitExecuteAsyncScript() {
        Object result = js().executeAsyncScript(current.getTarget());
        String varName = current.getValue();
        if (!Strings.isNullOrEmpty(varName)) {
            putVar(varName, result);

        }
    }

    /**
     * for each
     */
    public void emitForEach() {
        log.warn(MSG_SKIP_FLOW, Constants.str(current));
    }

    /**
     * if
     */
    public void emitIf() {
        log.warn(MSG_SKIP_FLOW, Constants.str(current));
    }

    /**
     * mouse down
     */
    public void emitMouseDown() {
        WebElement element = findElement(current.getTarget());
        new Actions(driver).moveToElement(element).clickAndHold().perform();
    }

    /**
     * mouse down at
     */
    public void emitMouseDownAt() {
        emitMouseDown();
    }

    /**
     * mouse move
     */
    public void emitMouseMove() {
        WebElement element = findElement(current.getTarget());
        new Actions(driver).moveToElement(element).perform();
    }

    /**
     * mouse move at
     */
    public void emitMouseMoveAt() {
        emitMouseMove();
    }

    /**
     * mouse out
     */
    public void emitMouseOut() {
        WebElement element = findElement(current.getTarget());
        new Actions(driver).moveToElement(element, 0, 0).perform();
    }

    /**
     * mouse over
     */
    public void emitMouseOver() {
        emitMouseMove();
    }

    /**
     * mouse up
     */
    public void emitMouseUp() {
        WebElement element = findElement(current.getTarget());
        new Actions(driver).moveToElement(element).release().perform();
    }

    /**
     * mouse Up At
     */
    public void emitMouseUpAt() {
        emitMouseUp();
    }

    /**
     * open
     */
    public void emitOpen() {
        final String url = Constants.concatUrl(record.getUrl(), current.getTarget());
        driver.get(url);
    }

    /**
     * pause
     */
    public void emitPause() {
        long sleepFor = Long.parseLong(current.getValue());
        try {
            Thread.sleep(sleepFor);
        } catch (InterruptedException e) {
            //不做任何处理
        }
    }

    /**
     * remove selection
     */
    public void emitRemoveSelection() {
        emitSelect();
    }

    /**
     * repeat if
     */
    public void emitRepeatIf() {
        log.warn(MSG_SKIP_FLOW, Constants.str(current));
    }

    /**
     * run
     */
    public void emitRun() {
        log.warn(MSG_SKIP_FLOW, Constants.str(current));
    }

    /**
     * run script
     */
    public void emitRunScript() {
        js().executeScript(current.getTarget());
    }

    /**
     * select frame
     */
    public void emitSelectFrame() {
        String frameLocation = checkNotNull(current.getTarget());
        if (Constants.RELATIVE_TOP.equals(frameLocation) || Constants.RELATIVE_PARENT.equals(frameLocation)) {
            driver.switchTo().defaultContent();
            return;
        }
        Optional<String> indexId = Constants.trimPrefix(frameLocation, Constants.INDEX_EQUAL);
        if (indexId.isPresent()) {
            int i = Integer.parseInt(indexId.get());
            driver.switchTo().frame(i);
            return;
        }
        WebElement element = findElement(frameLocation);
        driver.switchTo().frame(element);
    }

    /**
     * select Window
     */
    public void emitSelectWindow() {
        String target = checkNotNull(current.getTarget());
        String windowHandle = parseWindowHandle(target);
        driver.switchTo().window(windowHandle);

    }

    /**
     * send keys
     */
    public void emitSendKeys() {
        emitType();
    }

    /**
     * set speed
     */
    public void emitSetSpeed() {
        log.warn("'set speed' is a no-op in code export, use 'pause' insted");
    }

    /**
     * set window size
     */
    public void emitSetWindowSize() {
        Dimension dimension = Constants.parseDimension(current.getTarget());
        driver.manage().window().setSize(dimension);
    }


    /**
     * store
     */
    public void emitStore() {
        putVar(current.getTarget(), current.getTarget());
    }

    /**
     * store attribute
     */
    public void emitStoreAttribute() {
        String target = current.getTarget();
        int index = target.lastIndexOf('@');
        String locator = target.substring(0, index);
        String attribute = target.substring(index + 1);
        String attributeValue = findElement(locator).getAttribute(attribute);
        putVar(current.getValue(), attributeValue);
    }

    /**
     * store json
     */
    public void emitStoreJson() {
        String target = checkNotNull(current.getTarget());
        putVar(current.getValue(), JSON.parse(target));
    }

    /**
     * store text
     */
    public void emitStoreText() {
        String text = findElement(current.getTarget()).getText();
        putVar(current.getValue(), text);
    }

    /**
     * store title
     */
    public void emitStoreTitle() {
        putVar(current.getValue(), driver.getTitle());
    }

    /**
     * store value
     */
    public void emitStoreValue() {
        String text = findElement(current.getTarget()).getAttribute(VALUE);
        putVar(current.getValue(), text);
    }

    /**
     * store Window handle
     */
    public void emitStoreWindowHandle() {
        putVar(current.getValue(), driver.getWindowHandle());
    }

    /**
     * submit
     */
    public void emitSubmit() {
        throw new IllegalStateException("Selenium WebDriver不支持'submit',请重新录制");
    }

    /**
     * store Xpath count
     */
    public void emitStoreXpathCount() {
        int size = driver.findElements(Constants.parseBy(current.getTarget())).size();
        putVar(current.getValue(), size);
    }

    /**
     * times
     */
    public void emitTimes() {
        log.warn(MSG_SKIP_FLOW, Constants.str(current));
    }

    /**
     * uncheck
     */
    public void emitUncheck() {
        WebElement element = findElement(current.getTarget());
        if (element.isSelected()) {
            element.click();
        }
    }

    /**
     * verify
     */
    public void emitVerify() {
        assertText(() -> String.valueOf(vars.get(current.getTarget())));
    }

    /**
     * verify checked
     */
    public void emitVerifyChecked() {
        checkState(findElement(current.getTarget()).isSelected());
    }

    /**
     * verify editable
     */
    public void emitVerifyEditable() {
        emitAssertEditable();
    }

    /**
     * verify not editable
     */
    public void emitVerifyNotEditable() {
        emitAssertNotEditable();
    }

    /**
     * verify element present
     */
    public void emitVerifyElementPresent() {
        emitAssertElementPresent();
    }

    /**
     * verify element not present
     */
    public void emitVerifyElementNotPresent() {
        emitAssertElementNotPresent();
    }

    /**
     * verify not checked
     */
    public void emitVerifyNotChecked() {
        emitAssertNotChecked();
    }

    /**
     * verify not selected value
     */
    public void emitVerifyNotSelectedValue() {
        emitAssertNotSelectedValue();
    }

    /**
     * verify not text
     */
    public void emitVerifyNotText() {
        emitAssertNotText();
    }

    /**
     * verify selected label
     */
    public void emitVerifySelectedLabel() {
        emitAssertSelectedLabel();
    }

    /**
     * verify selected value
     */
    public void emitVerifySelectedValue() {
        emitAssertSelectedValue();
    }

    /**
     * verify text
     */
    public void emitVerifyText() {
        emitAssertText();
    }

    /**
     * verify title
     */
    public void emitVerifyTitle() {
        emitAssertTitle();
    }

    /**
     * verify value
     */
    public void emitVerifyValue() {
        emitAssertValue();
    }

    /**
     * wait for element editable
     */
    public void emitWaitForElementEditable() {
        By locator = Constants.parseBy(current.getTarget());
        driverWait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    /**
     * wait for element not editable
     */
    public void emitWaitForElementNotEditable() {
        By locator = Constants.parseBy(current.getTarget());
        driverWait.until(ExpectedConditions.not(ExpectedConditions.elementToBeClickable(locator)));
    }

    /**
     * wait for element not present
     */
    public void emitWaitForElementNotPresent() {
        By by = Constants.parseBy(current.getTarget());
        WebElement element = driver.findElement(by);
        driverWait.until(ExpectedConditions.stalenessOf(element));
    }

    /**
     * wait for element not visible
     */
    public void emitWaitForElementNotVisible() {
        By by = Constants.parseBy(current.getTarget());
        driverWait.until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    /**
     * wait for element present
     */
    public void emitWaitForElementPresent() {
        By locator = Constants.parseBy(current.getTarget());
        driverWait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    /**
     * wait for element visible
     */
    public void emitWaitForElementVisible() {
        By locator = Constants.parseBy(current.getTarget());
        driverWait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * webdriver answer on visible prompt
     */
    public void emitWebdriverAnswerOnVisiblePrompt() {
        emitAnswerOnNextPrompt();
    }

    /**
     * webdriver choose cancel on visible confirmation
     */
    public void emitWebdriverChooseCancelOnVisibleConfirmation() {
        driver.switchTo().alert().dismiss();
    }

    /**
     * webdriver choose cancel on visible prompt
     */
    public void emitWebdriverChooseCancelOnVisiblePrompt() {
        driver.switchTo().alert().dismiss();
    }

    /**
     * webdriver choose ok on visible confirmation
     */
    public void emitWebdriverChooseOkOnVisibleConfirmation() {
        driver.switchTo().alert().accept();
    }

    /**
     * while
     */
    public void emitWhile() {
        log.warn(MSG_SKIP_FLOW, Constants.str(current));
    }

    private void emitType() {
            findElement(current.getTarget()).sendKeys(Constants.parseKeys(current.getValue()));
    }

    private String parseWindowHandle(String target) {
        Optional<String> optional = Constants.trimPrefix(target, Constants.HANDLEQUAL);
        if (optional.isPresent()) {
            String handle = optional.get();
            Optional<String> handleVar = Constants.clampStr(handle, "${", "}");
            if (handleVar.isPresent()) {
                String varName = handleVar.get();
                Object varObject = getVar(varName);
                if (varObject == null) {
                    throw new IllegalStateException("未找到变量" + varName);
                }
                return varObject.toString();
            }
            return handle;
        }
        optional = Constants.trimPrefix(target, Constants.NAME_EQUAL);
        if (optional.isPresent()) {
            return optional.get();
        }

        optional = Constants.trimPrefix(target, Constants.WIN_SER);
        if (optional.isPresent()) {
            String s = optional.get();
            if (Constants.LOCAL.equals(s)) {
                ArrayList<String> handles = new ArrayList<>(driver.getWindowHandles());
                return handles.get(0);
            }
            int index = Integer.parseInt(s);
            ArrayList<String> handles = new ArrayList<>(driver.getWindowHandles());
            return handles.get(index);
        }
        return target;
    }


    private void assertText(Supplier<String> supplier) {
        String wanted = current.getValue();
        checkArgument(wanted != null, "规则错误：未指定value");
        String text = supplier.get();
        if (!wanted.equals(text)) {
            throw new IllegalStateException("预期内容为：[" + wanted + "],但是内容为[" + text + "]");
        }
    }

    private WebElement findElement(String target) {
        By by = Constants.parseBy(target);
        return driverWait.until(ExpectedConditions.visibilityOfElementLocated(by));
    }

}
