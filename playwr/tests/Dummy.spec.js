import { test, expect } from "@playwright/test";
import { registerUrl } from "../utils/urls";

const helper = "npx playwright test tests/Dummy.spec.js --project=chromium";

test.beforeEach(async ({ page }) => {
    await page.goto(registerUrl);
});

test("109-ValidateLoggingInWithTheDummyAccount @smoke", async ({ page }) => {
    await page.locator("//span[text()='Log In']").click();

    await page.getByRole("button", { name: "Use Dummy Account" }).click();

    await expect(page.getByAltText("Logo")).toBeVisible();
});

test.describe("110-run all the tests using the dummy account", () => {
    console.log("run all the tests using the dummy account")
});