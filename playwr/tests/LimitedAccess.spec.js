import { test, expect } from "@playwright/test";
import { url, registerUrl } from "../utils/urls";

test("001-UserNotLoggedInSuccesfullyOpensTheRegistrationLoginPage", async ({ page }) => {
    await page.goto(registerUrl);
    await expect(page.getByRole("heading", { name: "Sign Up" })).toBeVisible();
})

test("002-UserNotLoggedInTryingToAccessThePageRedirectedToTheLoginPage", async ({ page }) => {
    await page.goto(url);
    await expect(page.getByRole("heading", { name: "Sign Up" })).toBeVisible();
})