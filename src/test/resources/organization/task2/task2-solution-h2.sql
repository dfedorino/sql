WITH RECURSIVE EmployeeHierarchy(EmployeeID, Name, ManagerID, DepartmentID, RoleID)
                   AS (SELECT EmployeeID, Name, ManagerID, DepartmentID, RoleID
                       FROM Employees
                       WHERE EmployeeID = 1
                       UNION ALL
                       SELECT e.EmployeeID, e.Name, e.ManagerID, e.DepartmentID, e.RoleID
                       FROM Employees e
                                JOIN EmployeeHierarchy eh ON e.ManagerID = eh.EmployeeID)
SELECT eh.EmployeeID                                    AS EmployeeID,
       eh.Name                                          AS EmployeeName,
       eh.ManagerID                                     AS ManagerID,
       d.DepartmentName                                 AS DepartmentName,
       r.RoleName                                       AS RoleName,
       (SELECT STRING_AGG(DISTINCT p.ProjectName, ', ' ORDER BY p.ProjectName)
        FROM Projects p
        WHERE p.DepartmentID = eh.DepartmentID) AS ProjectNames,
       (SELECT STRING_AGG(t.TaskName, ', ' ORDER BY t.TaskName)
        FROM Tasks t
        WHERE t.AssignedTo = eh.EmployeeID)             AS TaskNames,
       (SELECT COUNT(*)
        FROM Tasks t
        WHERE t.AssignedTo = eh.EmployeeID)             AS TotalTasks,
       COALESCE((SELECT COUNT(*)
                 FROM Employees e
                 WHERE e.ManagerID = eh.EmployeeID), 0) AS TotalSubordinates
FROM EmployeeHierarchy eh
         JOIN Departments d ON eh.DepartmentID = d.DepartmentID
         JOIN Roles r ON eh.RoleID = r.RoleID
ORDER BY eh.Name;
