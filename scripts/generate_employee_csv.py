# helper script to generate_employees
# run instruction python3 scripts/generate_employee_csv.py;
#!/usr/bin/python3
import csv
import random

class Employee:
    def __init__(self, emp_id, first_name, last_name, salary=None, manager_id=None):
        self.emp_id = int(emp_id)
        self.first_name = first_name
        self.last_name = last_name
        self.salary = int(salary) if salary else None
        self.manager_id = int(manager_id) if manager_id else None
        self.subordinates = []

    def set_salary_based_on_subordinates(self):
        if self.subordinates and any(emp.salary for emp in self.subordinates):
            avg_salary = sum(emp.salary for emp in self.subordinates if emp.salary) / len([emp for emp in self.subordinates if emp.salary])
            self.salary = random.randint(int(avg_salary * 1.2), int(avg_salary * 1.5))  # Within expected bounds
        else:
            self.salary = random.randint(30000, 50000)  # Base salary for non-managers or if no subordinates have salaries

def generate_employees(num_employees):
    employees = []
    ceo = Employee(1, 'CEO', 'Boss', 100000)  # CEO with predefined salary
    employees.append(ceo)

    for emp_id in range(2, num_employees + 1):
        first_name = f"First{emp_id}"
        last_name = f"Last{emp_id}"
        # Randomly assign managers based on existing employees
        manager_id = random.choice([e.emp_id for e in employees if e.emp_id * 1.2 < emp_id])
        emp = Employee(emp_id, first_name, last_name, None, manager_id)
        employees.append(emp)
        employees[manager_id - 1].subordinates.append(emp)
        print(emp_id)

    random.shuffle(employees)

    for emp in employees:
        emp.set_salary_based_on_subordinates()

    return employees

def write_employees_to_csv(employees, filename='employee.csv'):
    with open(filename, mode='w', newline='') as file:
        writer = csv.writer(file)
        writer.writerow(['Id', 'firstName', 'lastName', 'salary', 'managerId'])
        for emp in employees:
            writer.writerow([emp.emp_id, emp.first_name, emp.last_name, emp.salary, emp.manager_id or ""])

employees = generate_employees(100_000)
write_employees_to_csv(employees)
