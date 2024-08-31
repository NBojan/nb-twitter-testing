import { test as setup, expect } from '@playwright/test';
import { registerUrl } from '../utils/urls';

const authFile = 'storage/user.json';

const validAccount = {email: "mytesting@testing.com", pass: "Testing123"};

setup('authenticate', async ({ page }) => {
    await page.goto(registerUrl);
    await page.locator("//span[text()='Log In']").click();
    await page.locator("input[name='email']").fill(validAccount.email);
    await page.locator("input[name='password']").fill(validAccount.pass);
    await page.getByRole("button", { name: "Submit" }).click();

    await expect(page.getByAltText("Logo")).toBeVisible();

    await page.context().storageState({ path: authFile });
});

// setup('authenticate', async ({ page }) => {
//     await page.goto(registerUrl);
//     await page.locator("//span[text()='Log In']").click();
//     await page.getByRole("button", { name: "Use Dummy Account" }).click();

//     await expect(page.getByAltText("Logo")).toBeVisible();

//     await page.context().storageState({ path: authFile });
// });