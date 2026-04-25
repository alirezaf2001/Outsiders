VISUAL SWING TESTS – README
===========================

These tests launch the real application window and programmatically drive each
panel to verify UI state, error messages, and input validation.  No JUnit or
other test framework is required – each file is a standalone Java program.

Test files
----------
  SwingTestUtils.java          Shared reflection helpers and dialog auto-closer
  LoginPanelTest.java          4 tests  – field clearing and initial state
  AddEmployeePanelTest.java    6 tests  – form reset and validation error dialogs
  SalaryUpdatePanelTest.java   5 tests  – form reset and percentage/number validation
  EmployeeSearchPanelTest.java 7 tests  – mode configuration, reset, and bad-input dialogs
  ReportsPanelTest.java        9 tests  – report-type row visibility, clear, bad-date dialog

Total: 31 automated visual checks


HOW TO COMPILE (from the project root)
---------------------------------------
Step 1 – compile the application source:
  javac -cp "src:lib/mysql-connector-j-9.6.0.jar" -d bin $(find src -name "*.java")

Step 2 – compile the test utilities and each test class:
  javac -cp "bin:lib/mysql-connector-j-9.6.0.jar" -d tests \
        tests/visual_tests/SwingTestUtils.java \
        tests/visual_tests/LoginPanelTest.java \
        tests/visual_tests/AddEmployeePanelTest.java \
        tests/visual_tests/SalaryUpdatePanelTest.java \
        tests/visual_tests/EmployeeSearchPanelTest.java \
        tests/visual_tests/ReportsPanelTest.java

  (On Windows replace ":" with ";" in the -cp argument.)


HOW TO RUN (from the project root)
------------------------------------
  java -cp "bin:lib/mysql-connector-j-9.6.0.jar:tests" visual_tests.LoginPanelTest
  java -cp "bin:lib/mysql-connector-j-9.6.0.jar:tests" visual_tests.AddEmployeePanelTest
  java -cp "bin:lib/mysql-connector-j-9.6.0.jar:tests" visual_tests.SalaryUpdatePanelTest
  java -cp "bin:lib/mysql-connector-j-9.6.0.jar:tests" visual_tests.EmployeeSearchPanelTest
  java -cp "bin:lib/mysql-connector-j-9.6.0.jar:tests" visual_tests.ReportsPanelTest

Each program opens a visible application window so you can watch what happens.
Dialogs appear on screen and are automatically dismissed after ~300 ms.
Results are printed to the console:

  [PASS] clearFields() – both fields become empty
  [FAIL] some test label
         reason why it failed

The program exits with code 0 (all pass) or 1 (at least one failure).


DATABASE REQUIREMENT
---------------------
Tests that exercise validation logic (bad number format, bad date format,
negative percentage, etc.) do NOT need a database connection – the input is
rejected before any service call is made.

Tests in EmployeeSearchPanelTest that need a session create a mock Employee
object in memory; no DB lookup is triggered.

If you want to run end-to-end tests that actually hit the database (real login,
real search results, etc.) make sure MySQL is running and the credentials in
src/db/DatabaseConfig.java are correct.
