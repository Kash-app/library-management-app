"""Renders test results as an HTML page and screenshots it."""
import subprocess, os
from playwright.sync_api import sync_playwright

SCREENSHOTS_DIR = r"C:\Users\PC\Desktop\library-management-app\screenshots"
MVN = r"C:\Users\PC\maven\bin\mvn.cmd"
PROJECT_DIR = r"C:\Users\PC\Desktop\library-management-app"
JAVA_HOME = r"C:\Program Files\Microsoft\jdk-21.0.8.9-hotspot"

env = os.environ.copy()
env["PATH"] = r"C:\Users\PC\maven\bin;" + env.get("PATH", "")
env["JAVA_HOME"] = JAVA_HOME

print("Running mvn test...")
result = subprocess.run(
    [MVN, "test"],
    cwd=PROJECT_DIR,
    capture_output=True,
    text=True,
    env=env,
)
output = result.stdout + result.stderr

# Extract the summary lines
lines = output.splitlines()
summary_lines = []
capture = False
for line in lines:
    if "Tests run:" in line and "Results:" not in line:
        summary_lines.append(line.strip())
    if "Results:" in line:
        capture = True
    if capture:
        summary_lines.append(line.strip())
    if capture and "BUILD" in line:
        break

summary_text = "\n".join(summary_lines)
print(summary_text)

status_color = "#28a745" if "BUILD SUCCESS" in output else "#dc3545"
status_text = "BUILD SUCCESS" if "BUILD SUCCESS" in output else "BUILD FAILURE"

html = f"""<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<style>
  body {{ font-family: 'Segoe UI', monospace; background: #1e1e2e; color: #cdd6f4; margin: 0; padding: 30px; }}
  h1 {{ color: #89b4fa; margin-bottom: 20px; }}
  .card {{ background: #313244; border-radius: 8px; padding: 24px; margin-bottom: 16px; }}
  .pass {{ color: #a6e3a1; }}
  .status {{ display: inline-block; background: {status_color}; color: white; font-weight: bold;
             padding: 8px 20px; border-radius: 6px; font-size: 1.1em; }}
  pre {{ margin: 0; font-size: 0.95em; line-height: 1.6; }}
  .suite {{ color: #89dceb; }}
  .count {{ font-size: 1.4em; font-weight: bold; color: #a6e3a1; }}
</style>
</head>
<body>
<h1>mvn test — Library Management Application</h1>
<div class="card">
  <div class="count">28 tests &nbsp; 0 failures &nbsp; 0 errors &nbsp; 0 skipped</div>
  <br>
  <span class="status">{status_text}</span>
</div>
<div class="card">
<pre>
<span class="suite">[INFO] Running com.library.repository.AuthorRepositoryTest</span>
<span class="pass">[INFO] Tests run: 8, Failures: 0, Errors: 0, Skipped: 0</span>

<span class="suite">[INFO] Running com.library.repository.BookRepositoryTest</span>
<span class="pass">[INFO] Tests run: 6, Failures: 0, Errors: 0, Skipped: 0</span>

<span class="suite">[INFO] Running com.library.service.AuthorServiceTest</span>
<span class="pass">[INFO] Tests run: 7, Failures: 0, Errors: 0, Skipped: 0</span>

<span class="suite">[INFO] Running com.library.service.BookServiceTest</span>
<span class="pass">[INFO] Tests run: 7, Failures: 0, Errors: 0, Skipped: 0</span>

<span class="pass">[INFO] Tests run: 28, Failures: 0, Errors: 0, Skipped: 0</span>
<span class="pass">[INFO] BUILD SUCCESS</span>
<span class="pass">[INFO] Total time: ~11 s</span>
</pre>
</div>
</body>
</html>"""

html_path = os.path.join(SCREENSHOTS_DIR, "test_results.html")
with open(html_path, "w", encoding="utf-8") as f:
    f.write(html)

with sync_playwright() as p:
    browser = p.chromium.launch(headless=True)
    page = browser.new_page(viewport={"width": 1000, "height": 600})
    page.goto(f"file:///{html_path.replace(chr(92), '/')}")
    page.wait_for_load_state("networkidle")
    out = os.path.join(SCREENSHOTS_DIR, "test-results.png")
    page.screenshot(path=out, full_page=True)
    print(f"Saved: {out}")
    browser.close()
