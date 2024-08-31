import path from "path";
import { url } from "../utils/urls";
import { test, expect } from "@playwright/test";
import { randomNumber, randomText } from "../utils/randomText";

const helper = "npx playwright test tests/UploadComment.spec.js --project=chromium";

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

test("076-ValidateTheCommentButtonOpensTheUploadCommentContainer @smoke", async ({ page }) => {
    const tweet = page.locator(anotherUserPostLocator);
    await tweet.locator("button.comment-btn").click();
    const uploadCommentModal = page.locator("//aside[contains(@id, 'closeModal') and contains(@class, 'z-50')]");
    await expect(uploadCommentModal).toBeVisible();
    await uploadCommentModal.getByRole("button").first().click();
    await expect(page.locator("#closeModal").filter({ hasText: "Reply" })).toBeHidden();
});

test("077-ValidatePostingACommentWithTextAndImageOnAPostFromAnotherUser @smoke", async ({ page }) => {
    const text = randomText(10);
    const rndNumb = randomNumber(5);
    const tweet = page.locator(anotherUserPostLocator);
    //open commentModal
    await tweet.locator("button.comment-btn").click();
    const uploadCommentModal = page.locator("//aside[contains(@id, 'closeModal') and contains(@class, 'z-50')]");
    await expect(uploadCommentModal).toBeVisible();
    //insert text
    await uploadCommentModal.locator("#comment-textarea").fill(text);
    //insert image
    const fileChooserPromise = page.waitForEvent('filechooser');
    await uploadCommentModal.locator("label").click();
    const fileChooser = await fileChooserPromise;
    await fileChooser.setFiles(path.join(__dirname, `../images/postPics/${rndNumb}.jpg`));
    await expect(page.getByAltText("selectedFile")).toBeVisible();
    //upload
    await uploadCommentModal.getByRole("button", { name: "Reply" }).click();
    //assert upload
    await expect(page.locator("#closeModal").filter({ hasText: "Reply" })).toBeHidden();
    await expect(page.locator(`//p[text()='${text}']`)).toBeVisible();
    await expect(page.url()).toContain("https://nb-twitter.vercel.app/tweet/");
});

test("078-ValidatePostingACommentWithTextOnAPostFromAnotherUser @smoke", async ({ page }) => {
    const text = randomText(10);
    const tweet = page.locator(anotherUserPostLocator);
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
});

test("079-ValidatePostingACommentOnAPersonalPostWithTextAndImage", async ({ page }) => {
    const text = randomText(10);
    const rndNumb = randomNumber(5);
    const tweet = page.locator(ownPostLocator);
    //open commentModal
    await tweet.locator("button.comment-btn").click();
    const uploadCommentModal = page.locator("//aside[contains(@id, 'closeModal') and contains(@class, 'z-50')]");
    await expect(uploadCommentModal).toBeVisible();
    //insert text
    await uploadCommentModal.locator("#comment-textarea").fill(text);
    //insert image
    const fileChooserPromise = page.waitForEvent('filechooser');
    await uploadCommentModal.locator("label").click();
    const fileChooser = await fileChooserPromise;
    await fileChooser.setFiles(path.join(__dirname, `../images/postPics/${rndNumb}.jpg`));
    await expect(page.getByAltText("selectedFile")).toBeVisible();
    //upload
    await uploadCommentModal.getByRole("button", { name: "Reply" }).click();
    //assert upload
    await expect(page.locator("#closeModal").filter({ hasText: "Reply" })).toBeHidden();
    await expect(page.locator(`//p[text()='${text}']`)).toBeVisible();
    await expect(page.url()).toContain("https://nb-twitter.vercel.app/tweet/");
});

test("080-ValidatePostingACommentOnAPersonalPostWithTextOnly", async ({ page }) => {
    const text = randomText(10);
    const tweet = page.locator(ownPostLocator);
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
});

test("081-ValidatePostingACommentFromThePostPage @smoke", async ({ page }) => {
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
});

test("082-ValidatePostingACommentFromAProfilePage @smoke", async ({ page }) => {
    const text = randomText(10);
    const tweet = page.locator(anotherUserPostLocator);
    //go to profile page
    await tweet.locator("p.post-username").click();
    await expect(page.locator("p.profile-name")).toBeVisible();
    await expect(page.url()).toContain("https://nb-twitter.vercel.app/profile/");
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
});

test.skip("083-ValidateTextboxDoesNotAcceptMoreThan280Chars", async ({ page }) => {
    const text = randomText(300);
    const tweet = page.locator(anotherUserPostLocator);
    //open commentModal
    await tweet.locator("button.comment-btn").click();
    const uploadCommentModal = page.locator("//aside[contains(@id, 'closeModal') and contains(@class, 'z-50')]");
    await expect(uploadCommentModal).toBeVisible();
    //insert text
    await uploadCommentModal.locator("#comment-textarea").fill(text);
    //assert
    await expect((await uploadCommentModal.locator("#comment-textarea").inputValue()).length).toBeLessThanOrEqual(280);
});

test("084-VerifyThereIsAPreviewOfTheImageAfterSelectingIt", async ({ page }) => {
    const rndNumb = randomNumber(5);
    const tweet = page.locator(anotherUserPostLocator);
    //open commentModal
    await tweet.locator("button.comment-btn").click();
    const uploadCommentModal = page.locator("//aside[contains(@id, 'closeModal') and contains(@class, 'z-50')]");
    await expect(uploadCommentModal).toBeVisible();
    //insert image
    const fileChooserPromise = page.waitForEvent('filechooser');
    await uploadCommentModal.locator("label").click();
    const fileChooser = await fileChooserPromise;
    await fileChooser.setFiles(path.join(__dirname, `../images/postPics/${rndNumb}.jpg`));
    //assert
    await expect(page.getByAltText("selectedFile")).toBeVisible();
});

test("085-ValidateRemovingASelectedImage", async ({ page }) => {
    const rndNumb = randomNumber(5);
    const tweet = page.locator(anotherUserPostLocator);
    //open commentModal
    await tweet.locator("button.comment-btn").click();
    const uploadCommentModal = page.locator("//aside[contains(@id, 'closeModal') and contains(@class, 'z-50')]");
    await expect(uploadCommentModal).toBeVisible();
    //insert image
    const fileChooserPromise = page.waitForEvent('filechooser');
    await uploadCommentModal.locator("label").click();
    const fileChooser = await fileChooserPromise;
    await fileChooser.setFiles(path.join(__dirname, `../images/postPics/${rndNumb}.jpg`));
    await expect(page.getByAltText("selectedFile")).toBeVisible();
    //remove image and assert
    await page.locator("//img[@alt='selectedFile']/parent::div").click();
    await expect(page.getByAltText("selectedFile")).toHaveCount(0);
});

test("086-ValidateReplyButtonIsDisabledIfNeitherTextOrImageInputted", async ({ page }) => {
    const tweet = page.locator(anotherUserPostLocator);
    //open commentModal
    await tweet.locator("button.comment-btn").click();
    const uploadCommentModal = page.locator("//aside[contains(@id, 'closeModal') and contains(@class, 'z-50')]");
    await expect(uploadCommentModal).toBeVisible();
    //assert
    await expect(uploadCommentModal.getByRole("button", { name: "Reply" })).toBeDisabled();
});
test("087-ValidateReplyButtonIsDisabledIfOnlyWhiteSpacesAreInputted", async ({ page }) => {
    const tweet = page.locator(anotherUserPostLocator);
    //open commentModal
    await tweet.locator("button.comment-btn").click();
    const uploadCommentModal = page.locator("//aside[contains(@id, 'closeModal') and contains(@class, 'z-50')]");
    await expect(uploadCommentModal).toBeVisible();
    //insert text
    await uploadCommentModal.locator("#comment-textarea").fill(" ");
    //assert
    await expect(uploadCommentModal.getByRole("button", { name: "Reply" })).toBeDisabled();
});
test.skip("088-ValidateReplyButtonIsDisabledIfWrongFormatOfFileIsSelected", async ({ page }) => {
    const tweet = page.locator(anotherUserPostLocator);
    //open commentModal
    await tweet.locator("button.comment-btn").click();
    const uploadCommentModal = page.locator("//aside[contains(@id, 'closeModal') and contains(@class, 'z-50')]");
    await expect(uploadCommentModal).toBeVisible();
    //insert wrong format
    const fileChooserPromise = page.waitForEvent('filechooser');
    await uploadCommentModal.locator("label").click();
    const fileChooser = await fileChooserPromise;
    await fileChooser.setFiles(path.join(__dirname, `../images/wrongFormat/1.pdf`));
    //assert
    await expect(uploadCommentModal.getByRole("button", { name: "Reply" })).toBeDisabled();
});