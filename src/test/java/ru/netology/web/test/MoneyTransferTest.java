package ru.netology.web.test;

import com.codeborne.selenide.Configuration;
import lombok.val;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class MoneyTransferTest {

    @Test
    void shouldTransferZeroCard() {
        Configuration.holdBrowserOpen = true;
        val loginPage = open("http://localhost:9999", LoginPage.class);
        val authInfo = DataHelper.getAuthInfo();
        val verificationPage = loginPage.validLogin(authInfo);
        val verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        val dashboardPage = verificationPage.validVerify(verificationCode);
        val balanceFirstCardBefore = dashboardPage.getFirstCardBalance();
        val balanceSecondCardBefore = dashboardPage.getSecondCardBalance();
        val transferPage = dashboardPage.secondCardDeposit();
        int amount = 0;
        transferPage.transferMoney(amount, DataHelper.getFirstCardNumber());
        val balanceFirstCardAfter = dashboardPage.getFirstCardBalance();
        val balanceSecondCardAfter = dashboardPage.getSecondCardBalance();
        assertEquals((balanceFirstCardBefore - amount), balanceFirstCardAfter);
        assertEquals((balanceSecondCardBefore + amount), balanceSecondCardAfter);
    }

    @Test
    void shouldTransferCardFirstNumber() {
        Configuration.holdBrowserOpen = true;
        val loginPage = open("http://localhost:9999", LoginPage.class);
        val authInfo = DataHelper.getAuthInfo();
        val verificationPage = loginPage.validLogin(authInfo);
        val verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        val dashboardPage = verificationPage.validVerify(verificationCode);
        val balanceFirstCardBefore = dashboardPage.getSecondCardBalance();
        val balanceSecondCardBefore = dashboardPage.getFirstCardBalance();
        val transferPage = dashboardPage.firstCardDeposit();
        int amount = 2100;
        transferPage.transferMoney(amount, DataHelper.getSecondCardNumber());
        val balanceSecondCardAfter = dashboardPage.getSecondCardBalance();
        val balanceFirstCardAfter = dashboardPage.getFirstCardBalance();
        assertEquals((balanceSecondCardBefore - amount), balanceSecondCardAfter);
        assertEquals((balanceFirstCardBefore + amount), balanceFirstCardAfter);
    }

    @Test
    void shouldTransferMoreThanAllFromFirstCard() {
        val loginPage = open("http://localhost:9999", LoginPage.class);
        val authInfo = DataHelper.getAuthInfo();
        val verificationPage = loginPage.validLogin(authInfo);
        val verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        val dashboardPage = verificationPage.validVerify(verificationCode);
        val balanceFirstCardBefore = dashboardPage.getFirstCardBalance();
        val balanceSecondCardBefore = dashboardPage.getSecondCardBalance();
        val transferPage = dashboardPage.secondCardDeposit();
        int amount = 183000;
        transferPage.transferMoney(amount, DataHelper.getFirstCardNumber());
        transferPage.failedTransfer();
    }
}
