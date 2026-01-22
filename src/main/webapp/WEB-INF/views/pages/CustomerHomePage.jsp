<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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

        <button class="navbar-toggler" type="button"
                data-bs-toggle="collapse" data-bs-target="#navbarNav">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav ms-auto">
                <li class="nav-item">
                    <a class="nav-link active" href="#">Home</a>
                </li>
                <li class="nav-item">
                    <!-- Products already loaded on page -->
                    <a class="nav-link" href="#products-section">Products</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="#">Cart</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="#">My Orders</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link text-danger"
                       href="${pageContext.request.contextPath}/logout">
                        Logout
                    </a>
                </li>
            </ul>
        </div>
    </div>
</nav>

<!-- Hero Section -->
<div class="hero-section">
    <h1>Furniture Online Shop</h1>
    <p>Premium Quality Furniture for Your Dream Home</p>
    <h5 class="mt-3">Welcome, <strong>${username}</strong></h5>
</div>

<!-- Categories -->
<%--<div class="container mt-5">
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
</div>--%>

<!-- Product List -->
<div class="container mt-5" id="products-section">
    <h3 class="text-center mb-4">Available Products</h3>

    <div class="row">

        <c:forEach var="product" items="${products}">
            <div class="col-md-4 col-sm-6 mb-4">

                <div class="card h-100 shadow-sm">
                    <div class="card-body">

                        <h5 class="card-title fw-bold">
                                ${product.name}
                        </h5>

                        <h6 class="card-subtitle mb-2 text-muted">
                                ${product.category.name}
                        </h6>

                        <p class="card-text">
                                ${product.description}
                        </p>

                        <p class="card-text">
                            <strong>Price:</strong> ₹${product.price}
                        </p>

                        <p class="card-text">
                            <strong>Stock:</strong> ${product.stockQuantity}
                        </p>

                    </div>
                </div>

            </div>
        </c:forEach>

        <c:if test="${empty products}">
            <div class="col-12 text-center text-muted">
                <p>No products available</p>
            </div>
        </c:if>

    </div>

</div>

<!-- Bootstrap JS -->
<script src="${pageContext.request.contextPath}/js/bootstrap.bundle.min.js"></script>

</body>
</html>
