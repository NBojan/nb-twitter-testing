import { test, expect } from "@playwright/test";
import { registerUrl } from "../utils/urls";

const helper = "npx playwright test tests/Login.spec.js --project=chromium";
const validAccount = {email: "mytesting@testing.com", pass: "Testing123"};
const invalidAccount = {email: "mytesting@testing.com", pass: "Bojan123"};

test.beforeEach(async ({ page }) => {
    await page.goto(registerUrl);
});

test("019-ValidateLoginByUsingValidEmailAndPassword @smoke", async ({ page }) => {
    await page.locator("//span[text()='Log In']").click();
    await page.locator("input[name='email']").fill(validAccount.email);
    await page.locator("input[name='password']").fill(validAccount.pass);
    await page.getByRole("button", { name: "Submit" }).click();
    await expect(page.getByAltText("Logo")).toBeVisible();
});

test("020-ValidateRedirectToTheDashboardAfterLogin", async ({ page }) => {
    await page.locator("//span[text()='Log In']").click();
    await page.locator("input[name='email']").fill(validAccount.email);
    await page.locator("input[name='password']").fill(validAccount.pass);
    await page.getByRole("button", { name: "Submit" }).click();
    await expect(page.getByAltText("Logo")).toBeVisible();
});

test("021-SubmitButtonIsDisabledIfOnlyOneFieldIsFilled", async ({ page }) => {
    await page.locator("//span[text()='Log In']").click();
    await page.locator("input[name='email']").fill(validAccount.email);
    await expect(page.getByRole("button", { name: "Submit" })).toBeDisabled();
});

test("022-SubmitButtonIsDisabledIfWhiteSpacesAreFilledInBothFields @smoke", async ({ page }) => {
    await page.locator("//span[text()='Log In']").click();
    await page.locator("input[name='email']").fill(" ");
    await page.locator("input[name='password']").fill(" ");
    await expect(page.getByRole("button", { name: "Submit" })).toBeDisabled();
});

test("023-ValidateErrorMessageUsingValidEmailButWrongPassword", async ({ page }) => {
    const errMsg = "Invalid email or password";
    await page.locator("//span[text()='Log In']").click();
    await page.locator("input[name='email']").fill(invalidAccount.email);
    await page.locator("input[name='password']").fill(invalidAccount.pass);
    await page.getByRole("button", { name: "Submit" }).click();
    await expect(page.locator("p.text-red-500")).toBeVisible();
    await expect(page.locator("p.text-red-500")).toHaveText(errMsg);
});

test("024-ValidateLinkToSwitchToSignUpIsVisible", async ({ page }) => {
    await page.locator("//span[text()='Log In']").click();
    await expect(page.locator("//span[text()='Sign Up']")).toBeVisible();
});

test("025-ValidateLinkToSwitchToSignUpSwitchesToTheRegisterForm @smoke", async ({ page }) => {
    await page.locator("//span[text()='Log In']").click();
    await page.locator("//span[text()='Sign Up']").click();
    await expect(page.getByRole("heading", { name: "Sign Up" })).toBeVisible();
});