info = "F,B{"

function update() {

    axios({
        method: 'get',
        url: 'http://localhost:8080/api/data',
        'Access-Control-Allow-Credentials': true
    })
        .then(function (res) {

            document.getElementById('LED_VALUE').innerText = res.data[1].value;
            if(parseInt(res.data[1].value)>0){
                val = 1;
                updateLightBulbIcon();
            }
            else{
                val = 0;
                updateLightBulbIcon();
            }
            document.getElementById('SERVO_VALUE').innerText = res.data[2].value;
            document.getElementById('PIR_VALUE').innerText = res.data[3].value;
            document.getElementById('LL_VALUE').innerText = res.data[4].value;

        })
        .catch(err => console.log(err));;
}

function post(id,value) {
    data = {
        command: info+String(id)+"}["+String(value)+"]"
    };
    axios({
        method: 'post',
        url: 'http://localhost:8080/api/data',
        'Access-Control-Allow-Origin': '*',
        'Content-Type': 'application/json',
        data: data
    }).then(res => console.log(res)).catch(function (err) {
        console.log(data);
        console.log(err);
    });
}

var val = 0;

function updateVal(){
    val = 1-val
    updateLightBulbIcon();
}

function updateLightBulbIcon(){
    if(val == 1){
        document.getElementById('1').innerText = "LIGHT OFF"
        document.getElementById('img').src = "./light-bulb-on.svg"
    }
    else{
        document.getElementById('1').innerText = "LIGHT ON"
        document.getElementById('img').src = "./light-bulb-off.svg"
    }
}


document.getElementById("1").addEventListener('click',function(event){

    
    updateVal();
     

    post(1,val);
});

//setInterval(update, 5000);