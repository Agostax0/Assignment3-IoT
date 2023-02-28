function update(){
    /*axios.get('http://localhost:8080/api/data/')
    .then(res => document.getElementById('valori').innerText=res.data)
    .catch(err=>console.log(err));*/

    axios({
        method: 'get',
        url: 'http://localhost:8080/api/data',
        'Access-Control-Allow-Credentials':true
    })
    .then(function(res){
        let lo = document.getElementById('valori');
        res.data.forEach(element => {
            let li = document.createElement('li');
            console.log(element);
            li.innerHTML = JSON.stringify(element)
            lo.appendChild(li);
        });
        
    })
    .catch(err=>console.log(err));;
}

function post(){
    data = {
        value: 1.1,
        place: "JS"
    };
    axios({
        method: 'post',
        url: 'http://localhost:8080/api/data',
        'Access-Control-Allow-Origin': '*',
        'Content-Type': 'application/json',
        data:data
    });
}