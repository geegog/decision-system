package com.inbank.decisionsystem.loan.rest;

import com.inbank.decisionsystem.DecisionSystemApplication;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.core.StringContains;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.StringContains.containsString;

@SpringBootTest(classes = DecisionSystemApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LoanControllerTests {

    @LocalServerPort
    private Integer port;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
    }

    @Test
    public void profileWithDebt() {
        given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "personalCode": "49002010965",
                          "loanAmount": 2000.67,
                          "periodInMonths": 12
                        }""")
                .when()
                .post("api/v1/loan/decision")
                .then()
                .assertThat()
                .statusCode(200)
                .body("canApproved",  equalTo(false))
                .body("hasDebt",  equalTo(true));

    }

    @Test
    public void profileWithoutDebtButWithNegativeDecision() {
        given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "personalCode": "49002010976",
                          "loanAmount": 4000.00,
                          "periodInMonths": 12
                        }""")
                .when()
                .post("api/v1/loan/decision")
                .then()
                .assertThat()
                .statusCode(200)
                .body("canApproved",  equalTo(false));

    }

    @Test
    public void profileWithoutDebtButWithPositiveDecisionL1() {
        given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "personalCode": "49002010976",
                          "loanAmount": 4000.00,
                          "periodInMonths": 48
                        }""")
                .when()
                .post("api/v1/loan/decision")
                .then()
                .assertThat()
                .statusCode(200)
                .body("canApproved",  equalTo(true))
                .body("approvableLoan.maxPossibleLoanAmount", equalTo(6000F))
                .body("approvableLoan.maxSuitablePeriod", equalTo(60));

    }

    @Test
    public void profileWithPositiveDecisionL2() {
        given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "personalCode": "49002010987",
                          "loanAmount": 4000.00,
                          "periodInMonths": 12
                        }""")
                .when()
                .post("api/v1/loan/decision")
                .then()
                .assertThat()
                .statusCode(200)
                .body("canApproved",  equalTo(false))
                .body("approvableLoan.maxPossibleLoanAmount", equalTo(3900F))
                .body("approvableLoan.maxSuitablePeriod", equalTo(13));

    }

    @Test
    public void profileWithPositiveDecisionL3() {
        given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "personalCode": "49002010998",
                          "loanAmount": 9000.00,
                          "periodInMonths": 12
                        }""")
                .when()
                .post("api/v1/loan/decision")
                .then()
                .assertThat()
                .statusCode(200)
                .body("canApproved",  equalTo(true))
                .body("approvableLoan.maxPossibleLoanAmount", equalTo(10000F))
                .body("approvableLoan.maxSuitablePeriod", equalTo(60));

    }

    @Test
    public void loanAmountRequestExceeded() {
        given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "personalCode": "49002010998",
                          "loanAmount": 12000.00,
                          "periodInMonths": 12
                        }""")
                .when()
                .post("api/v1/loan/decision")
                .then()
                .assertThat()
                .statusCode(400)
                .body(containsString("Maximum input sum can be 10000 Euros"));

    }

    @Test
    public void loanAmountRequestBelowMinimum() {
        given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "personalCode": "49002010998",
                          "loanAmount": 1000.00,
                          "periodInMonths": 12
                        }""")
                .when()
                .post("api/v1/loan/decision")
                .then()
                .assertThat()
                .statusCode(400)
                .body(containsString("Minimum input sum can be 2000 Euros"));

    }

    @Test
    public void loanPeriodRequestExceeded() {
        given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "personalCode": "49002010998",
                          "loanAmount": 2000.00,
                          "periodInMonths": 90
                        }""")
                .when()
                .post("api/v1/loan/decision")
                .then()
                .assertThat()
                .statusCode(400)
                .body(containsString("Maximum loan period can be 60 months"));

    }

    @Test
    public void loanPeriodRequestBelowMinimum() {
        given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "personalCode": "49002010998",
                          "loanAmount": 1000.00,
                          "periodInMonths": 11
                        }""")
                .when()
                .post("api/v1/loan/decision")
                .then()
                .assertThat()
                .statusCode(400)
                .body(containsString("Minimum loan period can be 12 months"));

    }

    @Test
    public void loanPeriodRequestWithInvalidPersonalCode() {
        given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "personalCode": "4900201099",
                          "loanAmount": 2000.00,
                          "periodInMonths": 12
                        }""")
                .when()
                .post("api/v1/loan/decision")
                .then()
                .assertThat()
                .statusCode(400)
                .body(containsString("Invalid personal code"));

    }

}
