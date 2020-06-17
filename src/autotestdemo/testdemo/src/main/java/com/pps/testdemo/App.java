package com.pps.testdemo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.management.ManagementFactory;
import java.net.URL;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.logging.Logger;

import java.util.logging.Level;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.grid.selenium.GridLauncherV3;
import org.openqa.grid.shared.Stoppable;

/**
 * Hello world!
 *
 */
public class App {
    static int m_nRunning = 0;
    static Logger m_logger_serv = null;
    static Logger m_logger_main = null;
    static FileHandler m_fileHandler = null;
    static ConsoleHandler m_consoleHandler = null;
    static Stoppable m_stoppable = null;
    static RemoteWebDriver m_webDriver = null;
    static ArrayList<String> arrayList = null;
    static FirefoxOptions desiredCapabilities = null;
    static String url_http = "http://";
    static String url_host = "127.0.0.1";
    static String url_port = "";
    static String url_mark = ":";
    static String url_path = "/wd/hub";
    static String url_role = "standalone";
    static String app_path = (new File(App.class.getProtectionDomain().getCodeSource().getLocation().getPath()))
            .getParent();
    static String temp_path = System.getProperty("java.io.tmpdir");
    static String logger_path = temp_path + File.separator + "log";
    static String logger_type = ".log";
    static String logger_date = "yyyyMMddHHmmss";
    static String driver_name = System.getProperty("os.name").toLowerCase().startsWith("win") ? "gd.win64"
            : "gd.linux64";// "gd.linux64"; //"gd.win64";
    static String driver_path = temp_path + File.separator + "geckodriver.exe";

    public static String getCurrentPID() {
        // get pid name representing the running Java virtual machine.
        return ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
    }

    public static void changeWorkpath(String workPath) {
        System.getProperties().setProperty("user.dir", workPath);
    }

    private static void extractResourceFileStream(InputStream is, String dest) {
        int bytesRead = 0;
        byte[] b = null;
        OutputStream os = null;
        try {
            os = new FileOutputStream(new File(dest));
            b = new byte[1024];
            while ((bytesRead = is.read(b)) > 0) {
                os.write(b, 0, bytesRead);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private static void copyFile(String source, String dest) {
        try {
            Files.copy(new File(source).toPath(), new File(dest).toPath());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static Logger initLogger(String loggerName, String loggerPath, String loggerType) {
        Logger _logger = null;
        try {
            _logger = Logger.getLogger(loggerName);
            _logger.setLevel(Level.ALL);

            m_consoleHandler = new ConsoleHandler();
            m_consoleHandler.setLevel(Level.ALL);
            m_fileHandler = new FileHandler(loggerPath + File.separator + loggerName + loggerType);
            m_fileHandler.setLevel(Level.ALL);
            m_fileHandler.setFormatter(new LogHander());
            _logger.addHandler(m_consoleHandler);
            _logger.addHandler(m_fileHandler);
        } catch (Exception e) {
            // TODO: handle exception
            _logger.info(e.getMessage());
            System.exit(0);
        }
        return _logger;
    }

    public static void prepare(String[] args) {
    }

    public static boolean running(String[] args) {
        return (m_nRunning == 1);
    }

    public static void startup(String[] args) {
        while (App.running(new String[] {})) {
            try {
                int nPort = (int) (4444 + Math.random() * (65535 - 4444 + 1));
                m_stoppable = new GridLauncherV3()
                        .launch(new String[] { "-host", url_host, "-port", String.valueOf(nPort), "-role", url_role });
                url_port = String.valueOf(nPort);
                m_logger_serv.info("service started!" + url_port);
                break;
            } catch (Exception e) {
                // TODO: handle exception
                m_logger_serv.info(e.getMessage());
                m_stoppable = null;
            }
        }
    }

    public static void cleanup(String[] args) {
        try {
            if (m_stoppable != null) {
                // Exit application
                m_stoppable.stop();
            }
            m_logger_serv.info("service stopped!");
        } catch (Exception e) {
            // TODO: handle exception
            // e.printStackTrace();
            m_logger_serv.info(e.getMessage());
            m_stoppable = null;
        }
    }

    public static void main(String[] args) {
        int m_index = 0;
        String m_pid = getCurrentPID();
        File logger_file = new File(logger_path);
        File driver_file = new File(driver_path);

        String os_name = System.getProperty("os.name");
        System.out.println("os.name=" + os_name);
        if (os_name.toLowerCase().startsWith("win")) {
            System.out.println(os_name + " Windows");
        }
        else {
            System.out.println(os_name + " Linux");            
        }
        ArrayList<String[]> config_string_array = CsvHander.csv_reader(app_path + File.separator + "config.csv");

        if (!driver_file.exists()) {
            extractResourceFileStream(App.class.getClassLoader().getResourceAsStream(driver_name), driver_path);
        }

        if (!driver_file.exists()) {
            initLogger("jvmapp-error[" + m_pid + "-]" + (new SimpleDateFormat(logger_date)).format(new Date()), ".",
                    logger_type).info("[" + m_pid + "]" + driver_file + " not failure.");
            System.exit(0);
        }
        try {
            Runtime.getRuntime().exec("chmod +x " + driver_path);
        } catch (Exception e) {
            // TODO: handle exception
        }

        System.setProperty("webdriver.gecko.driver", driver_path);

        logger_file.mkdirs();
        if (!logger_file.exists()) {
            initLogger("jvmapp-error[" + m_pid + "-]" + (new SimpleDateFormat(logger_date)).format(new Date()), ".",
                    logger_type).info("[" + m_pid + "]" + logger_path + " create failure.");
            System.exit(0);
        }
        changeWorkpath(temp_path);

        m_logger_serv = initLogger("jvmapp-serv" + (new SimpleDateFormat(logger_date)).format(new Date()), logger_path,
                logger_type);
        m_logger_main = initLogger("jvmapp-main" + (new SimpleDateFormat(logger_date)).format(new Date()), logger_path,
                logger_type);

        if (config_string_array.size() <= 0) {
            m_logger_main.info("config_string_array size=0!");
            System.exit(0);
        }
        m_nRunning = 1;

        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                App.startup(new String[] {});
            }
        }).start();

        //
        // String root = new File("").getAbsolutePath();
        // String driver = root + File.separator + "geckodriver.exe";

        m_logger_main.info("Enter process now!");

        // arrayList = new
        // ArrayList<>(Arrays.asList("https://login.airfrance.com/login/account",
        // "http://www.ppsbbs.tech",
        // "http://www.baidu.com", "http://www.ppsbbs.tech", "http://www.baidu.com",
        // "http://www.ppsbbs.tech"));
        arrayList = new ArrayList<>(Arrays.asList("https://map.baidu.com/search"));
        desiredCapabilities = new FirefoxOptions().setHeadless(true);

        while (running(new String[] {})) {
            try {
                if (url_port.length() > 0) {
                    m_logger_main.info("ArraySize=" + Integer.toString(arrayList.size()));
                    m_webDriver = new RemoteWebDriver(new URL(url_http + url_host + url_mark + url_port + url_path),
                            desiredCapabilities);
                    for (; m_index < arrayList.size();) {
                        String s = arrayList.get(m_index);
                        m_webDriver.get(s);
                        // webdriver.switchTo().window(webdriver.getWindowHandles().toArray()[0].toString());

                        // Search on ppsbbs
                        // m_logger.info("A text is - "
                        // +m_webDriver.findElement(By.tagName("a")).getText());
                        m_logger_main
                                .info("A href is - " + m_webDriver.findElement(By.tagName("a")).getAttribute("href"));

                        //
                        // WebDriver自带了一个智能等待的方法。
                        // manage.timeouts().implicitlyWait(arg0, arg1）；
                        // Arg0：等待的时间长度，int 类型 ；
                        // Arg1：等待时间的单位 TimeUnit.SECONDS 一般用秒作为单位。
                        //
                        // manage.timeouts().implicitlyWait(3, TimeUnit.SECONDS);

                        m_logger_main.info("Page title is - " + m_webDriver.getTitle());
                        m_logger_main.info("Current url is - " + m_webDriver.getCurrentUrl());
                        // Display number of results on Console
                        // System.out.println("Total Results - " +
                        // webdriver.findElement(By.id("kw")).getTagName());
                        try {
                            for (String[] strings : config_string_array) {
                                String str_sole_input = strings[0];
                                ArrayList<String[]> str_data_list = new ArrayList<String[]>();
                                m_webDriver.findElement(By.id("sole-input")).clear();
                                m_webDriver.findElement(By.id("sole-input")).sendKeys(str_sole_input);
                                m_webDriver.findElement(By.id("search-button")).click();
                                Thread.sleep(1000);
                                int current_page_index = 0;
                                boolean have_next_page = true;
                                while (have_next_page == true) {
                                    current_page_index += 1;
                                    have_next_page = false;
                                    try {
                                        List<WebElement> we_poilist = m_webDriver.findElements(By.className("poilist"));
                                        for (WebElement we_poiitem : we_poilist) {
                                            try {
                                                List<WebElement> we_poiitem_li_list = we_poiitem
                                                        .findElements(By.tagName("li"));
                                                for (WebElement we_poiitem_li : we_poiitem_li_list) {
                                                    try {
                                                        WebElement we_poiitem_li_ml_30 = we_poiitem_li
                                                                .findElement(By.className("ml_30"));
                                                        String strRow = we_poiitem_li_ml_30
                                                                .findElement(By.className("row")).getText();
                                                        String strAddr = we_poiitem_li_ml_30
                                                                .findElement(By.className("addr")).getText();
                                                        String strTel = we_poiitem_li_ml_30
                                                                .findElement(By.className("tel")).getText();

                                                        if (strRow.length() > 0 && strAddr.length() > 0
                                                                && strTel.length() > 0) {
                                                            str_data_list.add(new String[] { "\"" + strRow + "\"",
                                                                    "\"" + strAddr + "\"", "\"" + strTel + "\"" });
                                                            m_logger_main
                                                                    .info(current_page_index + " Page: This data is: "
                                                                            + strRow + "," + strAddr + "," + strTel);
                                                        }
                                                    } catch (Exception e) {
                                                        // TODO: handle exception
                                                        m_logger_main.info(current_page_index
                                                                + " Page: Data Error pass " + e.getMessage());
                                                    }
                                                }
                                            } catch (Exception e) {
                                                // TODO: handle exception
                                                m_logger_main.info(current_page_index + " Page: Poillist_li Error pass "
                                                        + e.getMessage());
                                            }
                                        }
                                    } catch (Exception e) {
                                        // TODO: handle exception
                                        m_logger_main.info(
                                                current_page_index + " Page: Poillist Error pass " + e.getMessage());
                                    }
                                    try {
                                        List<WebElement> we_poi_page_a_list = m_webDriver.findElement(By.id("poi_page"))
                                                .findElements(By.tagName("a"));
                                        for (WebElement we_poi_page_a_item : we_poi_page_a_list) {
                                            try {
                                                if (we_poi_page_a_item.isEnabled() && we_poi_page_a_item
                                                        .getAttribute("tid").equals("toNextPage")) {
                                                    if (we_poi_page_a_item.getAttribute("class")
                                                            .contains("next-none") == false) {
                                                        we_poi_page_a_item.click();
                                                        m_logger_main
                                                                .info(current_page_index + " Page: Click Next Page");
                                                        have_next_page = true;
                                                        break;
                                                    }
                                                }
                                            } catch (Exception e) {
                                                // TODO: handle exception
                                                m_logger_main.info(current_page_index
                                                        + " Page: NextPageItem Error pass " + e.getMessage());
                                            }
                                        }
                                    } catch (Exception e) {
                                        // TODO: handle exception
                                        m_logger_main.info(
                                                current_page_index + " Page: NextPage Error pass " + e.getMessage());
                                    }
                                    Thread.sleep(1000);
                                }
                                CsvHander.csv_writer(new String[] { "name", "addr", "tel" }, str_data_list,
                                        app_path + File.separator + str_sole_input + "-output.csv", false);
                            }
                        } catch (Exception e) {
                            // TODO: handle exception
                            m_logger_main.info(e.getMessage());
                        }

                        // m_logger_main.info("Page title is - " + m_webDriver.getTitle());
                        // m_logger_main.info("Current url is - " + m_webDriver.getCurrentUrl());

                        m_webDriver.manage().deleteAllCookies();
                        m_logger_main.info(s);
                        m_logger_main.info(Integer.toString(++m_index));
                        Thread.sleep(1000);
                    }
                    m_logger_main.info("End ArraySize=" + Integer.toString(arrayList.size()));
                    // System.in.read();
                    if (m_webDriver != null) {
                        //
                        // dr.quit()和dr.close()都可以退出浏览器,简单的说一下两者的区别：第一个close，
                        // 如果打开了多个页面是关不干净的，它只关闭当前的一个页面。第二个quit，
                        // 是退出了所有Webdriver所有的窗口，退的非常干净，所以推荐使用quit作为一个case退出的方法。
                        //
                        m_webDriver.quit();// 退出浏览器
                        m_logger_main.info("m_webDriver.quit()");
                    }
                    m_webDriver = null;
                    // Exit application
                    m_nRunning = 0;
                    m_logger_main.info("for(String s : arrayList) exit");
                } else {
                    Thread.sleep(1000);
                }
            } catch (Exception e) {
                // TODO: handle exception
                if (m_webDriver != null) {
                    //
                    // dr.quit()和dr.close()都可以退出浏览器,简单的说一下两者的区别：第一个close，
                    // 如果打开了多个页面是关不干净的，它只关闭当前的一个页面。第二个quit，
                    // 是退出了所有Webdriver所有的窗口，退的非常干净，所以推荐使用quit作为一个case退出的方法。
                    //
                    m_webDriver.quit();// 退出浏览器
                    m_logger_main.info("m_webDriver.quit()");
                }
                m_webDriver = null;
                continue;
            }
        }
        m_logger_main.info("all completed");
        App.cleanup(new String[] {});
        System.exit(0);
    }
}