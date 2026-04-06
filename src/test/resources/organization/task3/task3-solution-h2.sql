WITH RECURSIVE
    EmployeeHierarchy(EmployeeID, Name, ManagerID, DepartmentID, RoleID)
        AS (SELECT EmployeeID, Name, ManagerID, DepartmentID, RoleID
            FROM Employees
            WHERE EmployeeID = 1
            UNION ALL
            SELECT e.EmployeeID, e.Name, e.ManagerID, e.DepartmentID, e.RoleID
            FROM Employees e
                     JOIN EmployeeHierarchy eh ON e.ManagerID = eh.EmployeeID),
    Descendants(ManagerID, EmployeeID) AS (SELECT ManagerID, EmployeeID
                                           FROM Employees
                                           WHERE ManagerID IS NOT NULL
                                           UNION ALL
                                           SELECT d.ManagerID, e.EmployeeID
                                           FROM Descendants d
                                                    JOIN Employees e ON e.ManagerID = d.EmployeeID),
    SubCount(EmployeeID, TotalSubordinates) AS (SELECT ManagerID, COUNT(DISTINCT EmployeeID)
                                                FROM Descendants
                                                GROUP BY ManagerID)
SELECT eh.EmployeeID                            AS EmployeeID,
       eh.Name                                  AS EmployeeName,
       eh.ManagerID                             AS ManagerID,
       d.DepartmentName                         AS DepartmentName,
       r.RoleName                               AS RoleName,
       (SELECT STRING_AGG(DISTINCT p.ProjectName, ', ' ORDER BY p.ProjectName)
        FROM Projects p
        WHERE p.DepartmentID = eh.DepartmentID) AS ProjectNames,
       (SELECT STRING_AGG(t.TaskName, ', ' ORDER BY t.TaskName)
        FROM Tasks t
        WHERE t.AssignedTo = eh.EmployeeID)     AS TaskNames,
       sc.TotalSubordinates
FROM EmployeeHierarchy eh
         JOIN Departments d ON eh.DepartmentID = d.DepartmentID
         JOIN Roles r ON eh.RoleID = r.RoleID
         JOIN SubCount sc ON eh.EmployeeID = sc.EmployeeID
WHERE r.RoleName = 'Менеджер'
  AND sc.TotalSubordinates > 0
ORDER BY eh.Name;
