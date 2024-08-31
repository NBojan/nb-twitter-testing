import { test, expect } from "@playwright/test";
import { registerUrl } from "../utils/urls";
import { randomText, randomTextCyrilic } from "../utils/randomText";

test.beforeEach( async ({ page }) => {
    await page.goto(registerUrl);
});

test.describe("ValidTests", () => {

    test.skip("003-ValidateSignupWithValidDataInAllFields @smoke", async ({ page }) => {
        await page.locator("input[name='firstName']").fill("FirstName");
        await page.locator("input[name='lastName']").fill("LastName");
        await page.locator("input[name='username']").fill(randomText(8));
        await page.locator("input[name='email']").fill(`${randomText(8)}@gmail.com`);
        await page.locator("input[name='password']").fill("Bojan123");
        await page.getByRole("button", { name: "Submit" }).click();
        await expect(page.getByAltText("Logo")).toBeVisible();
    })
    
    test.skip("004-ValidateSignupWithCyrilicLettersInFirstLastAndUsername @smoke", async ({ page }) => {
        await page.locator("input[name='firstName']").fill("Име");
        await page.locator("input[name='lastName']").fill("Презиме");
        await page.locator("input[name='username']").fill(randomTextCyrilic(8));
        await page.locator("input[name='email']").fill(`${randomText(8)}@gmail.com`);
        await page.locator("input[name='password']").fill("Bojan123");
        await page.getByRole("button", { name: "Submit" }).click();
        await expect(page.getByAltText("Logo")).toBeVisible();
    })
    
    test.skip("005-ValidateSignupWithOneLetterFirstLastAndUsername", async ({ page }) => {
        await page.locator("input[name='firstName']").fill("А");
        await page.locator("input[name='lastName']").fill("Б");
        await page.locator("input[name='username']").fill(randomText(1));
        await page.locator("input[name='email']").fill(`${randomText(8)}@gmail.com`);
        await page.locator("input[name='password']").fill("Bojan123");
        await page.getByRole("button", { name: "Submit" }).click();
        await expect(page.getByAltText("Logo")).toBeVisible();
    })
    
    test.skip("006-ValidateSignupWithUsingUsernameWith24Characters", async ({ page }) => {
        await page.locator("input[name='firstName']").fill("firstName");
        await page.locator("input[name='lastName']").fill("lastName");
        await page.locator("input[name='username']").fill(randomText(24));
        await page.locator("input[name='email']").fill(`${randomText(8)}@gmail.com`);
        await page.locator("input[name='password']").fill("Bojan123");
        await page.getByRole("button", { name: "Submit" }).click();
        await expect(page.getByAltText("Logo")).toBeVisible();
    })
    
    test("007-ValidateUsernameWontAcceptMoreThan24Characters @smoke", async ({ page }) => {
        const username = randomText(30);
        await page.locator("input[name='username']").fill(username);
        expect((await page.locator("input[name='username']").inputValue()).length).toBeLessThanOrEqual(24);
    })
    
    test("008-ValidateSubmitButtonIsDisabledIfAtLeastOneFieldIsEmpty", async ({ page }) => {
        await page.locator("input[name='firstName']").fill("firstName");
        await page.locator("input[name='lastName']").fill("lastName");
        await page.locator("input[name='username']").fill(randomText(24));
        await page.locator("input[name='email']").fill(`${randomText(8)}@gmail.com`);
        await expect(page.getByRole("button", { name: "Submit" })).toBeDisabled();
    })
    
    test("009-ValidateSubmitButtonIsDisabledIfWhiteSpacesAreInputtedInAllTheFields @smoke", async ({ page }) => {
        await page.locator("input[name='firstName']").fill(" ");
        await page.locator("input[name='lastName']").fill(" ");
        await page.locator("input[name='username']").fill(" ");
        await page.locator("input[name='email']").fill(` `);
        await page.locator("input[name='password']").fill(" ");
        await expect(page.getByRole("button", { name: "Submit" })).toBeDisabled();
    })
    
    test("010-VerifyEmailPlaceholderIsEmail@Address.Com", async ({ page }) => {
        const placeholder = "email@address.com";
        expect(await page.locator("input[name='email']").getAttribute("placeholder")).toMatch(placeholder);
    })

    test("017-ValidateLinkToSwitchToLogInIsVisible", async ({ page }) => {
        await expect(page.locator("//span[text()='Log In']")).toBeVisible();
    })

    test("018-ValidateLinkToSwitchToLogInSwitchesToLoginForm @smoke", async ({ page }) => {
        await page.locator("//span[text()='Log In']").click();
        await expect(page.getByRole("heading", { name: "Login" })).toBeVisible();
    })
})



test.describe("InvalidTests", () => {

    test("011-ValidateErrorMessageIfUsernameIsAlreadyInUse", async ({ page }) => {
        const usernameInUse = "dummies";
        const errMessage = "Username is already in use";
        await page.locator("input[name='firstName']").fill("FirstName");
        await page.locator("input[name='lastName']").fill("LastName");
        await page.locator("input[name='username']").fill(usernameInUse);
        await page.locator("input[name='email']").fill(`${randomText(10)}@gmail.com`);
        await page.locator("input[name='password']").fill("Bojan123");
        await page.getByRole("button", { name: "Submit" }).click();
        await expect(page.locator("p.text-red-500")).toBeVisible();
        await expect(page.locator("p.text-red-500")).toHaveText(errMessage);
    })

    test("012-ValidateErrorMessageIfEmailIsAlreadyInUse", async ({ page }) => {
        const emailInUse = "dummy@dummies.com";
        const errMessage = "Email is already in use";
        await page.locator("input[name='firstName']").fill("FirstName");
        await page.locator("input[name='lastName']").fill("LastName");
        await page.locator("input[name='username']").fill(randomText(10));
        await page.locator("input[name='email']").fill(emailInUse);
        await page.locator("input[name='password']").fill("Bojan123");
        await page.getByRole("button", { name: "Submit" }).click();
        await expect(page.locator("p.text-red-500")).toBeVisible();
        await expect(page.locator("p.text-red-500")).toHaveText(errMessage);
    })

    test("013-ValidateErrorMessageIfPasswordIsLessThan8Chars", async ({ page }) => {
        const wrongPass = "Pass123";
        const errMessage = "Invalid password. Min. 8 charachters, at least 1 lowercase, 1 uppercase and 1 number.";
        await page.locator("input[name='firstName']").fill("FirstName");
        await page.locator("input[name='lastName']").fill("LastName");
        await page.locator("input[name='username']").fill(randomText(10));
        await page.locator("input[name='email']").fill(`${randomText(10)}@gmail.com`);
        await page.locator("input[name='password']").fill(wrongPass);
        await page.getByRole("button", { name: "Submit" }).click();
        await expect(page.locator("p.text-red-500")).toBeVisible();
        await expect(page.locator("p.text-red-500")).toHaveText(errMessage);
    })

    test("014-ValidateErrorMessageIfPasswordDoesNotIncludeLowercaseLetters", async ({ page }) => {
        const wrongPass = "PASSWORD123";
        const errMessage = "Invalid password. Min. 8 charachters, at least 1 lowercase, 1 uppercase and 1 number.";
        await page.locator("input[name='firstName']").fill("FirstName");
        await page.locator("input[name='lastName']").fill("LastName");
        await page.locator("input[name='username']").fill(randomText(10));
        await page.locator("input[name='email']").fill(`${randomText(10)}@gmail.com`);
        await page.locator("input[name='password']").fill(wrongPass);
        await page.getByRole("button", { name: "Submit" }).click();
        await expect(page.locator("p.text-red-500")).toBeVisible();
        await expect(page.locator("p.text-red-500")).toHaveText(errMessage);
    })

    test("015-ValidateErrorMessageIfPasswordDoesNotIncludeUppercaseLetters", async ({ page }) => {
        const wrongPass = "password123";
        const errMessage = "Invalid password. Min. 8 charachters, at least 1 lowercase, 1 uppercase and 1 number.";
        await page.locator("input[name='firstName']").fill("FirstName");
        await page.locator("input[name='lastName']").fill("LastName");
        await page.locator("input[name='username']").fill(randomText(10));
        await page.locator("input[name='email']").fill(`${randomText(10)}@gmail.com`);
        await page.locator("input[name='password']").fill(wrongPass);
        await page.getByRole("button", { name: "Submit" }).click();
        await expect(page.locator("p.text-red-500")).toBeVisible();
        await expect(page.locator("p.text-red-500")).toHaveText(errMessage);
    })

    test("016-ValidateErrorMessageIfPasswordDoesNotIncludeNumbers", async ({ page }) => {
        const wrongPass = "wrongPASSWORD";
        const errMessage = "Invalid password. Min. 8 charachters, at least 1 lowercase, 1 uppercase and 1 number.";
        await page.locator("input[name='firstName']").fill("FirstName");
        await page.locator("input[name='lastName']").fill("LastName");
        await page.locator("input[name='username']").fill(randomText(10));
        await page.locator("input[name='email']").fill(`${randomText(10)}@gmail.com`);
        await page.locator("input[name='password']").fill(wrongPass);
        await page.getByRole("button", { name: "Submit" }).click();
        await expect(page.locator("p.text-red-500")).toBeVisible();
        await expect(page.locator("p.text-red-500")).toHaveText(errMessage);
    })
})
