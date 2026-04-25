MANUAL TESTS README
===================

This folder holds the manual test checklist for the Outsiders Employee
Management System.

Files
-----
- manual_test_cases.csv
    Master checklist for manual testing. It contains both visual UI checks and
    logical/functional checks in one Excel-friendly sheet.

Why CSV instead of a binary .xlsx file
--------------------------------------
The checklist is stored as CSV so it can be:
- opened directly in Microsoft Excel, Google Sheets, or Numbers
- reviewed in git without binary diffs
- edited quickly by multiple team members

Open manual_test_cases.csv in Excel and use:
- "Comma delimited" when importing, if Excel asks
- wrap text on the Test Steps / Expected Result columns
- filters on Test Type, Module, Role, and Status

Suggested execution order
-------------------------
1. Run the visual checks first to confirm the UI is readable.
2. Run the employee-role logical checks.
3. Run the HR logical checks.
4. Run destructive checks last:
   - Add Employee
   - Delete Employee
   - Salary Update

Cleanup guidance
----------------
- Use dedicated test employee data for add / update / delete flows.
- For salary-update testing, note the original salaries before applying the
  update so the database can be restored after the test.
- Mark any blocked case clearly in the Status column and explain why in Notes.
