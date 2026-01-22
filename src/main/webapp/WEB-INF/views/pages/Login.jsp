<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login Page</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
    <style>
        body {
            height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
            background: linear-gradient(135deg, #6a11cb 0%, #2575fc 100%);
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }
        .login-card {
            background: #fff;
            padding: 2rem;
            border-radius: 15px;
            box-shadow: 0 8px 25px rgba(0, 0, 0, 0.2);
            width: 100%;
            max-width: 400px;
        }
        .login-card h2 {
            text-align: center;
            margin-bottom: 1.5rem;
            color: #333;
        }
        .btn-primary {
            background-color: #6a11cb;
            border: none;
        }
        .btn-primary:hover {
            background-color: #2575fc;
        }
        .form-text {
            font-size: 0.8rem;
            color: #6c757d;
        }
    </style>
</head>
<body>
<div class="login-card">
    <h2>Welcome Online Furniture Shop</h2>
    <c:if test="${not empty error}">
        <div class="alert alert-danger text-center" role="alert" id="errorMsg">
            <strong>${error}</strong>
        </div>
    </c:if>

    <form id="loginForm" >
        <div class="mb-3">
            <label for="userId" class="form-label">User ID</label>
            <input type="text" class="form-control" id="userName" name="userName" placeholder="Enter your User ID" required>
        </div>
        <div class="mb-3">
            <label for="password" class="form-label">Password</label>
            <input type="password" class="form-control" id="password" name="password" placeholder="Enter your Password" required>
        </div>

        <button type="submit" class="btn btn-primary w-100">Login</button>
        <small class="form-text d-block mt-3 text-center">
            Not have an account? <a href="/register" class="text-decoration-none">Click here</a>
        </small>
    </form>
</div>
<script src="${pageContext.request.contextPath}/js/bootstrap.bundle.min.js"></script>
<script>
    document.getElementById("loginForm").addEventListener("submit", function (e) {
        e.preventDefault();

        fetch("${pageContext.request.contextPath}/api/auth/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                userName: document.getElementById("userName").value,
                password: document.getElementById("password").value
            })
        })
            .then(res => {
                if (res.redirected) {
                    window.location.href = res.url; // ✅ follow redirect
                } else {
                    return res.text();
                }
            })
            .then(html => {
                if (html) {
                    document.open();
                    document.write(html);
                    document.close();
                }
            })
            .catch(() => alert("Login failed"));
    });

</script>
</body>
</html>
