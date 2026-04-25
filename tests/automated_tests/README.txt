AUTOMATED TEST CASES – README
==============================

test_cases.csv contains 30 structured test cases covering every major feature
of the Outsiders Employee Management System.  Open it in Microsoft Excel or
Google Sheets and fill in the last four columns as you execute each test.


HOW TO USE
----------
1. Open test_cases.csv in Excel (File > Open, choose "Delimited" with comma).
2. Assign tests to team members by adding their name to the "Tester Name" column.
3. Execute each test following the steps in the "Test Steps" column.
4. Record the actual observed result in "Actual Result".
5. Mark "Status" as:
     Pass     – actual result matches expected result exactly
     Fail     – actual result differs from expected result
     Blocked  – the test could not run (missing data, environment issue, etc.)
6. Use "Notes" for any extra observations, bug IDs, or follow-up items.
7. Share the completed file with the team lead for sign-off.


TEST COVERAGE SUMMARY
----------------------
Module                       Test IDs       Count
Login                        TC-01 – TC-05    5
Employee Search (HR)         TC-06 – TC-11    6
Employee Search (Employee)   TC-12 – TC-13    2
Add Employee                 TC-14 – TC-17    4
Edit / Delete Employee       TC-18 – TC-20    3
Bulk Salary Update           TC-21 – TC-24    4
Payroll History              TC-25 – TC-26    2
Reports                      TC-27 – TC-29    3
Session & Navigation         TC-30            1
                                          ------
TOTAL                                        30


PRECONDITIONS (apply to most tests)
-------------------------------------
- MySQL is running locally on port 3306.
- The employeeData database is populated with at least a few employee records,
  including one HR employee (division ID = 3) and one non-HR employee.
- The application is started fresh (java -cp "bin:lib/*" Main_gui.MainFrame or
  via the IDE run button) before beginning a test session.


COLUMN DESCRIPTIONS
--------------------
Test ID          Unique identifier (TC-01 … TC-30)
Module           Feature area being tested
Test Case Name   Short description of what the test verifies
Preconditions    State that must exist before the test can run
Test Steps       Numbered actions the tester must perform
Expected Result  What should happen if the system works correctly
Actual Result    What the tester observed (fill this in)
Status           Pass / Fail / Blocked (fill this in)
Tester Name      Who ran the test (fill this in)
Date Tested      Date the test was executed (fill this in)
Notes            Any extra context, bug reference, or follow-up (optional)
