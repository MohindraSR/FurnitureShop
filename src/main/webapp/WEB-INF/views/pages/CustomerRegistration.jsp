<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registration Page</title>
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
        .register-card {
            background: #fff;
            padding: 2rem;
            border-radius: 15px;
            box-shadow: 0 8px 25px rgba(0, 0, 0, 0.2);
            width: 100%;
            max-width: 400px;
        }
        .register-card h2 {
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
<div class="register-card">
    <h2>Create Your Account</h2>
    <form action="api/auth/register" method="POST">
        <div class="mb-3">
            <label for="userName" class="form-label">User ID</label>
            <input type="text" class="form-control" id="userName" name="userName" placeholder="Enter your User ID" required>
        </div>
        <div class="mb-3">
            <label for="password" class="form-label">Password</label>
            <input type="text" class="form-control" id="password" name="password" placeholder="Generate Password" required>
        </div>
        <div class="mb-3">
            <label for="customerName" class="form-label">Full Name</label>
            <input type="text" class="form-control" id="name" name="name" placeholder="Enter your Full Name" required>
        </div>
        <div class="mb-3">
            <label for="email" class="form-label">Email Address</label>
            <input type="email" class="form-control" id="email" name="email" placeholder="Enter your Email" required>
        </div>
        <div class="mb-3">
            <label for="address" class="form-label">Address</label>
            <textarea class="form-control" id="address" name="address" rows="2" placeholder="Enter your Address" required></textarea>
        </div>

        <button type="submit" class="btn btn-primary w-100">Register</button>
        <small class="form-text d-block mt-3 text-center">
            Already have an account? <a href="login" class="text-decoration-none">Login here</a>
        </small>
    </form>
</div>
<script src="${pageContext.request.contextPath}/js/bootstrap.bundle.min.js"></script>
</body>
</html>
