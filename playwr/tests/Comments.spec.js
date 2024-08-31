import { url } from "../utils/urls";
import { test, expect } from "@playwright/test";
import { randomText } from "../utils/randomText";

const helper = "npx playwright test tests/Comments.spec.js --project=chromium";

test.use({
    storageState: "storage/user.json",
})

test.beforeEach( async ({ page }) => {
    await page.goto(url);
});

const postWithComments = "https://nb-twitter.vercel.app/tweet/WZMIjKLqQV2pIIpFJ22q";
const personalCommentLocator = "//p[text()='forTestingComment']/ancestor::article";
const notPersonalCommentLocator = "//p[text()='forever']/ancestor::article";

test("097-ValidateLikingAndRemovingTheLikeOnAComment @smoke", async ({ page }) => {
    await page.goto(postWithComments);
    const post = page.locator(notPersonalCommentLocator);
    await expect(post).toBeVisible();
    //like
    await post.locator("button.like-btn").click();
    await expect(post.locator("button.dislike-btn")).toBeVisible();
    //remove like
    await post.locator("button.dislike-btn").click();
    await expect(post.locator("button.like-btn")).toBeVisible();
});

test("098-VerifyThereIsNoCommentButtonOnAComment", async ({ page }) => {
    await page.goto(postWithComments);
    const post = page.locator(notPersonalCommentLocator);
    await expect(post).toBeVisible();
    //asses
    await expect(post.locator("button.comment-btn")).toHaveCount(0);
});

test("099-ValidateOptionToRemoveCommentIsVisibleForAPersonalComment", async ({ page }) => {
    await page.goto(postWithComments);
    const post = page.locator(personalCommentLocator);
    await expect(post).toBeVisible();
    //asses
    await expect(post.locator("button.delete-btn")).toBeVisible();
});

test("100-ValidateOptionToRemoveCommentIsNotVisibleForAnotherUserComment", async ({ page }) => {
    await page.goto(postWithComments);
    const post = page.locator(notPersonalCommentLocator);
    await expect(post).toBeVisible();
    //asses
    await expect(post.locator("button.delete-btn")).toHaveCount(0);
});

test("101-ValidatePostingAndRemovingAComment", async ({ page }) => {
    //TC 081-ValidatePostingACommentFromThePostPage
    const anotherUserPostLocator = "//p[text()='Forza Milan Sempre!!']/ancestor::article";
    const text = randomText(10);
    const tweet = page.locator(anotherUserPostLocator);
    //go to post page
    await tweet.locator("p.post-text").click();
    await expect(page.getByRole("heading", { name: "Replies" })).toBeVisible();
    await expect(page.url()).toContain("https://nb-twitter.vercel.app/tweet/");
    //open commentModal
    await tweet.locator("button.comment-btn").click();
    const uploadCommentModal = page.locator("//aside[contains(@id, 'closeModal') and contains(@class, 'z-50')]");
    await expect(uploadCommentModal).toBeVisible();
    //insert text
    await uploadCommentModal.locator("#comment-textarea").fill(text);
    //upload
    await uploadCommentModal.getByRole("button", { name: "Reply" }).click();
    //assert upload
    await expect(page.locator("#closeModal").filter({ hasText: "Reply" })).toBeHidden();
    await expect(page.locator(`//p[text()='${text}']`)).toBeVisible();
    await expect(page.url()).toContain("https://nb-twitter.vercel.app/tweet/");
    //locate new comment and remove
    const newComment = page.locator(`//p[text()='${text}']/ancestor::article`);
    page.on('dialog', dialog => dialog.accept());
    await newComment.locator("button.delete-btn").click();
    await expect(page.locator(`//p[text()='${text}']/ancestor::article`)).toHaveCount(0);
    await expect(newComment).toHaveCount(0);
});
