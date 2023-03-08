<?php header("Access-Control-Allow-Origin: *"); ?>
<!DOCTYPE html>
<html>

<head>
    <script src="./axios.min.js"></script>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-rbsA2VBKQhggwzxH7pPCaAqO46MgnOM80zW1RWuH61DGLwZJEdK2Kadq2F9CUG65" crossorigin="anonymous">
    <link href="./style.css">
</head>


<body class="container">

    <div class="row">
        <div class="col-6">
            <button onclick="update()">GET</button>
            <table class="table table-bordered">
                <thead>
                    <tr>
                        <th>
                            Name
                        </th>
                        <th>
                            Value
                        </th>
                    </tr>
                </thead>
                <tbody class="table-group-divider">
                    <tr>
                        <th id="LED">LED</th>
                        <td id="LED_VALUE">-1</td>
                    </tr>
                    <tr>
                        <th id="SERVO">SERVO</th>
                        <td id="SERVO_VALUE">-1</td>
                    </tr>
                    <tr>
                        <th id="PIR">PIR</th>
                        <td id="PIR_VALUE">-1</td>
                    </tr>
                    <tr>
                        <th id="LL">LL</th>
                        <td id="LL_VALUE">-1</td>
                    </tr>
                    
                </tbody>
            </table>
        </div>

        <div class="col-6">
            <div class="row">
                <div class="col-3">
                <button id=1>LIGHT ON</button>
                <img id="img" src="./light-bulb-off.svg" height="40" width="40" />
                </div>

                <div class="col-3">
                    <button>BLINDS</button>  
                    <input type="range" class="form-range" min="0" max="180" value="0" id="2" onchange="post(2,this.value)">
                </div>
            </div>
        </div>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-kenU1KFdBIe4zVF0s0G1M5b4hcpxyD9F7jL+jjXkk+Q2h455rYXK/7HAuoJl+0I4" crossorigin="anonymous"></script>
    <script src="./file.js"></script>
</body>


</html>