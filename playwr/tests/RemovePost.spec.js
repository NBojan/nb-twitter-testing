import { url } from "../utils/urls";
import { test, expect } from "@playwright/test";
import { randomText } from "../utils/randomText";

const helper = "npx playwright test tests/RemovePost.spec.js --project=chromium";

test.use({
    storageState: "storage/user.json",
})

test.beforeEach( async ({ page }) => {
    await page.goto(url);
});

// dummies and mytestingaccount need to have NO likes on ownPost and anotherUserPost to begin with.

const ownPostLocator = "//p[text()='TestingPurposesPostDontTouch']/ancestor::article";
// const ownPostLocator = "//p[text()='Hello from me dummies.']/ancestor::article"; //for dummmy account test
const anotherUserPostLocator = "//p[text()='Forza Milan Sempre!!']/ancestor::article";

test("089-ValidateOptionToRemoveAPostIsVisibleForPersonalPost @smoke", async ({ page }) => {
    const tweet = page.locator(ownPostLocator);
    await expect(tweet.locator("button.delete-btn")).toBeVisible();
});

test("090-ValidateOptionToRemoveAPostIsNotVisibleForAnotherUsersPost @smoke", async ({ page }) => {
    const tweet = page.locator(anotherUserPostLocator);
    await expect(tweet.locator("button.delete-btn")).toHaveCount(0);
});
test("091-ValidatePostingAndRemovingAPost @smoke", async ({ page }) => {
    // TC 055 - ValidateUploadingAPostByUsingOnlyText
    const text = randomText(10);
    const mainSection = page.locator("section.flex-1");
    await mainSection.locator("textarea[id='main-textarea']").fill(text);
    await mainSection.getByRole("button", { name: "Tweet" }).click();
    await mainSection.locator("textarea[id='main-textarea']").clear();
    await expect(page.getByText(text)).toBeVisible();
    // locate and remove the post
    const tweet = page.locator(`//p[text()='${text}']/ancestor::article`);
    page.on('dialog', dialog => dialog.accept());
    await tweet.locator("button.delete-btn").click();
    await expect(page.getByText(text)).toHaveCount(0);
    await expect(page.locator(`//p[text()='${text}']/ancestor::article`)).toHaveCount(0);
});

test.skip("092-ValidateRemovingPostAfterPostingAndGoingToThePostPage @smoke", async ({ page }) => {
    // TC 055 - ValidateUploadingAPostByUsingOnlyText
    const text = randomText(10);
    const mainSection = page.locator("section.flex-1");
    await mainSection.locator("textarea[id='main-textarea']").fill(text);
    await mainSection.getByRole("button", { name: "Tweet" }).click();
    await mainSection.locator("textarea[id='main-textarea']").clear();
    await expect(page.getByText(text)).toBeVisible();
    // locate post
    const tweet = page.locator(`//p[text()='${text}']/ancestor::article`);
    // go to post page
    await tweet.locator("p.post-text").click();
    await expect(page.getByRole("heading", { name: "Replies" })).toBeVisible();
    await expect(page.url()).toContain("https://nb-twitter.vercel.app/tweet/");
    // remove
    page.on('dialog', dialog => dialog.accept());
    await tweet.locator("button.delete-btn").click();
    //asserts
    await expect(mainSection.locator("textarea[id='main-textarea']")).toBeVisible();
    await expect(page.url()).toMatch(url);
    await expect(page.getByText(text)).toHaveCount(0);
    await expect(page.locator(`//p[text()='${text}']/ancestor::article`)).toHaveCount(0);
});

test("093-ValidateRemovingPostAfterPostingAndGoingToTheProfilePage @smoke", async ({ page }) => {
    // TC 055 - ValidateUploadingAPostByUsingOnlyText
    const text = randomText(10);
    const mainSection = page.locator("section.flex-1");
    await mainSection.locator("textarea[id='main-textarea']").fill(text);
    await mainSection.getByRole("button", { name: "Tweet" }).click();
    await mainSection.locator("textarea[id='main-textarea']").clear();
    await expect(page.getByText(text)).toBeVisible();
    // locate post
    const tweet = page.locator(`//p[text()='${text}']/ancestor::article`);
    // go to profile page
    await tweet.locator("p.post-username").click();
    await expect(page.locator("p.profile-name")).toBeVisible();
    await expect(page.url()).toContain("https://nb-twitter.vercel.app/profile/");
    // remove
    page.on('dialog', dialog => dialog.accept());
    await tweet.locator("button.delete-btn").click();
    //asserts
    await expect(page.getByText(text)).toHaveCount(0);
    await expect(page.locator(`//p[text()='${text}']/ancestor::article`)).toHaveCount(0);
});
