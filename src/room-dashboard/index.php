<?php header("Access-Control-Allow-Origin: *"); ?>
<!DOCTYPE html>
<html>
<head>
    <script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
</head>
<body>
    <button onclick="update()">GET</button>

    <button onclick="post()">POST</button>
    <br>
    <lo id="valori"></lo>

    <script src="./file.js"></script>
</body>


</html>