<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Save Success</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
    <style>
        .progress {
            height: 5px;
            margin-top: 10px;
        }
    </style>
</head>
<body>
<div class="container mt-5">
    <div class="alert alert-success alert-dismissible fade show" role="alert">
        <strong>Success!</strong> Data saved successfully.
        <div class="progress">
            <div id="progressBar" class="progress-bar bg-success" role="progressbar" style="width: 100%;"
                 aria-valuenow="100" aria-valuemin="0" aria-valuemax="100"></div>
        </div>
    </div>
</div>

<script>
    // Total countdown time in milliseconds
    const totalTime = 3000; // 3 seconds
    const progressBar = document.getElementById('progressBar');
    let startTime = Date.now();

    const timer = setInterval(function() {
        let elapsed = Date.now() - startTime;
        let percent = 100 - (elapsed / totalTime) * 100;
        if (percent <= 0) {
            percent = 0;
            clearInterval(timer);
            window.location.href = '${pageContext.request.contextPath}/login';
        }
        progressBar.style.width = percent + '%';
    }, 50); // update every 50ms for smooth animation
</script>
<script src="${pageContext.request.contextPath}/js/bootstrap.bundle.min.js"></script>
</body>
</html>
