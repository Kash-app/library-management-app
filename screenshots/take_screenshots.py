from playwright.sync_api import sync_playwright
import os

SCREENSHOTS_DIR = r"C:\Users\PC\Desktop\library-management-app\screenshots"

with sync_playwright() as p:
    browser = p.chromium.launch(headless=True)
    page = browser.new_page(viewport={"width": 1280, "height": 900})

    # 1. Book list page (home)
    page.goto("http://localhost:8080")
    page.wait_for_load_state("networkidle")
    path1 = os.path.join(SCREENSHOTS_DIR, "book-list.png")
    page.screenshot(path=path1, full_page=True)
    print(f"Saved: {path1}")

    # 2. Add book form
    page.goto("http://localhost:8080/books/new")
    page.wait_for_load_state("networkidle")
    path2 = os.path.join(SCREENSHOTS_DIR, "add-book-form.png")
    page.screenshot(path=path2, full_page=True)
    print(f"Saved: {path2}")

    # 3. Add author form
    page.goto("http://localhost:8080/authors/new")
    page.wait_for_load_state("networkidle")
    path3 = os.path.join(SCREENSHOTS_DIR, "add-author-form.png")
    page.screenshot(path=path3, full_page=True)
    print(f"Saved: {path3}")

    browser.close()
    print("All screenshots taken successfully.")
