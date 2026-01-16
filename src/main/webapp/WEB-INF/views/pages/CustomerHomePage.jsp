<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Customer Home | Furniture Online Shop</title>

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css">

    <style>
        body {
            background-color: #f8f9fa;
        }

        .hero-section {
            background: linear-gradient(to right, #6a11cb, #2575fc);
            color: white;
            padding: 60px 20px;
            text-align: center;
        }

        .hero-section h1 {
            font-size: 3rem;
            font-weight: bold;
        }

        .hero-section p {
            font-size: 1.2rem;
        }
    </style>
</head>

<body>

<!-- Navbar -->
<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container-fluid">
        <a class="navbar-brand" href="#">Furniture Shop</a>

        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav ms-auto">
                <li class="nav-item">
                    <a class="nav-link active" href="#">Home</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="#" id="products">Products</a>

                </li>
                <li class="nav-item">
                    <a class="nav-link" href="/cart">Cart</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="/orders">My Orders</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link text-danger" href="/logout">Logout</a>
                </li>
            </ul>
        </div>
    </div>
</nav>

<!-- Hero Section -->
<div class="hero-section">
    <h1>Furniture Online Shop</h1>
    <p>Premium Quality Furniture for Your Dream Home</p>
    <h5 class="mt-3">Welcome, ${username}</h5>
</div>

<!-- Content Section -->
<div class="container mt-5">
    <div class="row text-center">
        <div class="col-md-4">
            <div class="card shadow-sm p-3">
                <h4>🛋 Living Room</h4>
                <p>Stylish sofas, tables, and decor</p>
            </div>
        </div>

        <div class="col-md-4">
            <div class="card shadow-sm p-3">
                <h4>🛏 Bedroom</h4>
                <p>Comfortable beds and wardrobes</p>
            </div>
        </div>

        <div class="col-md-4">
            <div class="card shadow-sm p-3">
                <h4>🪑 Office</h4>
                <p>Modern office furniture</p>
            </div>
        </div>
    </div>
</div>

<!-- Bootstrap JS -->
<script src="${pageContext.request.contextPath}/js/bootstrap.bundle.min.js"></script>
<script>
    document.getElementById("products").addEventListener("click", function (e) {
        e.preventDefault(); // stop page navigation

        fetch("${pageContext.request.contextPath}/api/user/allProducts", {
            method: "GET",
            headers: {
                "Authorization": "Bearer " + sessionStorage.getItem("jwt"),
                "Content-Type": "application/json"
            }
        })
            .then(res => {
                if (!res.ok) throw new Error("Unauthorized");
                return res.json();
            })
            .then(data => {
                console.log("Products:", data);
                // render products here
            })
            .catch(err => console.error(err));
    });
</script>

</body>
</html>
