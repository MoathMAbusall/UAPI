package com.azmqalabs.uapitestautomation.common;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.Markup;
import com.azmqalabs.uapitestautomation.apppages.GlobalConstant;
import com.azmqalabs.uapitestautomation.apppages.masterpages.BasePage;
import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.Connection;
import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;

public class Log extends BasePage {

    public Log(WebDriver driver, ExtentTest test) {
        this.driver = driver;
        this.test = test;
    }

    public void ReportEvent(String sStatus, String sDec) {

        if (sStatus.equalsIgnoreCase("pass")) {
            test.log(Status.PASS, SuccessFontColorPrefix + sDec + SuccessFontColorSuffix);
        }
        if (sStatus.equalsIgnoreCase("fail")) {
            test.log(Status.FAIL, ErrorFontColorPrefix + sDec + ErrorFontColorSuffix);
        }
        if ((sStatus.equalsIgnoreCase("info")) || (sStatus.equalsIgnoreCase(""))) {
            test.log(Status.INFO, INFOFontColorPrefix + sDec + INFOFontColorSuffix);
        }
//		if ((sStatus.equalsIgnoreCase("fatal"))) {
//			test.log(Status.FAIL, FatalFontColorPrefix + sDec + FatalFontColorSuffix);
//		}
    }

    public void ReportEvent(String sStatus, Markup sDec) {

        if (sStatus.equalsIgnoreCase("pass")) {

            test.log(Status.PASS, SuccessFontColorPrefix + sDec + SuccessFontColorSuffix);
        }
        if (sStatus.equalsIgnoreCase("fail")) {
            test.log(Status.FAIL, ErrorFontColorPrefix + sDec + ErrorFontColorSuffix);
        }
        if ((sStatus.equalsIgnoreCase("info")) || (sStatus.equalsIgnoreCase(""))) {
            test.log(Status.INFO, INFOFontColorPrefix + sDec + INFOFontColorSuffix);
        }
//		if ((sStatus.equalsIgnoreCase("fatal"))) {
//			test.log(Status.FAIL, FatalFontColorPrefix + sDec + FatalFontColorSuffix);
//		}
    }

    public void LogTestStep(String sStatus, String sDescription) {
//		if (sStatus.equalsIgnoreCase("fatal"))
//			test.log(Status.FAIL, sDescription);
        if (sStatus.equalsIgnoreCase("fail"))
            test.log(Status.FAIL, sDescription);
        else if (sStatus.equalsIgnoreCase("pass"))
            test.log(Status.PASS, sDescription);
        else if (sStatus.equalsIgnoreCase("info"))
            test.log(Status.INFO, sDescription);
    }

    public void PostTestStatus(String testScriptID) {
        String testScenarioName = testScriptID;
        Status testingStatus = test.getStatus();
        String testFinalStatus = "";

        testFinalStatus = testingStatus.toString();
        System.out.println("EXTENT REPORT STATUS: " + testingStatus);

        if (testFinalStatus.equalsIgnoreCase("pass"))
            test.log(Status.PASS, "TEST EXECUTION COMPLETED: " + testScenarioName);
        else if (testFinalStatus.equalsIgnoreCase("error"))
            test.log(Status.WARNING, "TEST SCENARIO VERIFICATION FAILED: " + testScenarioName);
        else if (testFinalStatus.equalsIgnoreCase("fail"))
            test.log(Status.FAIL, "TEST SCENARIO FAILED: " + testScenarioName);
//		else if (testFinalStatus.equalsIgnoreCase("fatal"))
//			test.log(Status.FAIL, "TEST SCENARIO HARD FAILED (EXCEPTION) " + testScenarioName);
        else if (testFinalStatus.equalsIgnoreCase(""))
            test.log(Status.FAIL, "TEST SCENARIO HARD FAILED (EXCEPTION) " + testScenarioName);
    }

    public void PostTestStatusIntoExcel(String testScriptID, String Tabname, String APPID) throws FilloException {
        String sFilename = "";
        Status testingStatus = test.getStatus();
        String sBankName = Config.GetProperty("BANK.NAME");
        File classpathRoot = new File(System.getProperty("user.dir"));
        File app = new File(classpathRoot.getAbsolutePath() + "//src//test//resources//testdata//",Config.Get("TESTDATA.LOCATION"));
        sFilename = app.toString();
        Connection connectionDB;
        Recordset recordset;
        String strQuery = "";
        Fillo fillo = new Fillo();
        connectionDB = fillo.getConnection(sFilename);
        strQuery = "Select * from " + Tabname;
        System.out.println(strQuery);
        recordset = connectionDB.executeQuery(strQuery);
        recordset.next();
        String insertstrQuery = "Update " + Tabname + " Set APPID='" + APPID + "' , ExecutionStatus='" + testingStatus
                + "' where TestScriptID='" + testScriptID + "'";
        System.out.println(insertstrQuery);
        connectionDB.executeUpdate(insertstrQuery);
    }

    public void PostTestStatus(Exception e) {
        String testScenarioName = "";
        Status testingStatus = test.getStatus();
        String testFinalStatus = "";

        testFinalStatus = testingStatus.toString();
        System.out.println("EXTENT REPORT STATUS: " + testingStatus);

        test.log(Status.INFO, "TEST EXECUTION COMPLETED: " + testScenarioName);
        if (testFinalStatus.equalsIgnoreCase("pass"))
            test.log(Status.PASS, "TEST EXECUTION COMPLETED: " + testScenarioName);
        else if (testFinalStatus.equalsIgnoreCase("error"))
            test.log(Status.WARNING, "TEST SCENARIO VERIFICATION FAILED: " + testScenarioName);
        else if (testFinalStatus.equalsIgnoreCase("fail"))
            test.log(Status.FAIL, "TEST SCENARIO FAILED: " + testScenarioName);
        else if (testFinalStatus.equalsIgnoreCase("fatal"))
            test.log(Status.FAIL, "TEST SCENARIO HARD FAILED (EXCEPTION) " + testScenarioName);
        else if (testFinalStatus.equalsIgnoreCase(""))
            test.log(Status.FAIL, "TEST SCENARIO HARD FAILED (EXCEPTION) " + testScenarioName);

        ReportEvent("FATAL",
                ErrorFontColorPrefix + "Moved To Driver Exception Exception - " + e + ErrorFontColorSuffix);
        ReportEvent("FATAL",
                ErrorFontColorPrefix + "Moved To Driver Exception Exception - Error found at class: "
                        + this.getClass().getName() + " Code line Number: "
                        + new Exception().getStackTrace()[0].getLineNumber() + "!" + ErrorFontColorSuffix);
    }

    public void QAMachineInfo() throws Exception {
        ReportEvent("INFO", "Test Execution By:: " + System.getProperty("user.name"));
        ReportEvent("INFO", "Test Execution Machine:: " + InetAddress.getLocalHost().getHostName());
        ReportEvent("INFO", "Test Execution Cycle:: " + Config.GetProperty("TEST.CYCLENAME"));
        ReportEvent("INFO", "Test Started @:: " + System.nanoTime());

    }
}
