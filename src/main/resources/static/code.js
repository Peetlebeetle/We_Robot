function addToCart(id, username) {
    console.log("addToCart test id: "+id);

    var xhr = new XMLHttpRequest();
    xhr.open("POST", "/product/add?id="+id);
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.send(username);
}

function removeFromCart(id) {
    console.log("removeFromCart test id: "+id);

    var xhr = new XMLHttpRequest();
    xhr.onload=deleteFromCart;
    xhr.open("GET", "/product/remove?id="+id);
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.send();

    document.getElementById("productName").value="";
}

function deleteFromCart(){
    var id = this.responseText;
    var table = document.getElementById("checkoutTable");
    var row = document.getElementById("row_" + id);
    table.deleteRow(row.rowIndex);

    cartProceedWithItem();
}

function viewProduct(id) {
    console.log("viewProduct test, "+id);

    var xhr = new XMLHttpRequest();
    xhr.open("GET", "/product?id="+id);
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.send();
}

function accountLogin(){
    var record = {
        name: document.getElementById("usrlogin").value,
        password: document.getElementById("pwdlogin").value,
    };

    var xhr = new XMLHttpRequest();
    xhr.open("POST", "/account/login");
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.send(JSON.stringify(record));

    document.getElementById("usrlogin").value="";
    document.getElementById("pwdlogin").value="";
}


function addAccount(){
    var record = {
        name: document.getElementById("usr").value,
        password: document.getElementById("pwd").value,
    };

    var xhr = new XMLHttpRequest();
    xhr.open("POST", "/account/add");
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.send(JSON.stringify(record));

    document.getElementById("usr").value="";
    document.getElementById("pwd").value="";
}

function addProduct(){

    var record = {
        imagepath: document.getElementById("imagepath").value,
        name: document.getElementById("name").value,
        description: document.getElementById("description").value,
        price: document.getElementById("price").value,
    };

    var xhr = new XMLHttpRequest();
    xhr.onload = insertProduct;
    xhr.open("POST", "/shop/product/add");
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.send(JSON.stringify(record));

}

function insertProduct() {
    var record = JSON.parse(this.responseText);
    var table = document.getElementById("productTable");
    var rows = table.querySelectorAll("tr");
    var row = table.insertRow(rows.length-1);
    row.id="row_"+record.id;
    var image_cell = row.insertCell(0);
    var name_cell = row.insertCell(1);
    var description_cell = row.insertCell(2);
    var price_cell = row.insertCell(3);
    var hide_cell = row.insertCell(4);
    var edit_cell = row.insertCell(5);

    image_cell.innerHTML=record.imagepath;
    name_cell.innerHTML=record.name;
    description_cell.innerHTML=record.description;
    price_cell.innerHTML=record.price;
    hide_cell.innerHTML="<button onclick='hideProduct("+record.id+")'>Hide</button>";
    edit_cell.innerHTML="<button onclick='editProduct("+record.id+"); refreshShop(this);'>Edit</button>";

    document.getElementById("imagepath").value="";
    document.getElementById("name").value="";
    document.getElementById("description").value="";
    document.getElementById("price").value="";
    document.getElementById("imagepath").focus();
}

function addToOrder(id, username) {
    console.log("addToOrder test id: "+id);

    var xhr = new XMLHttpRequest();
    xhr.open("POST", "/product/add?id="+id);
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.send(username);
}

function hideProduct(id){
    var xhr = new XMLHttpRequest();
    xhr.open("Get", "/product/hide?id="+id);
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.send();
}

function cartProceedWithItem(){
    var table = document.getElementById("checkoutTable");
    var button = document.getElementById("hideIfEmpty");
    if (table.rows.length == 0) {
        button.style.display = "none";
    }
}

function editProduct(id){
    var xhr = new XMLHttpRequest();
    xhr.onload = viewProduct;
    xhr.open("Get", "/editProduct/"+id);
    xhr.responseType = "document";
    xhr.setRequestHeader("Content-Type", "application/xml");
    xhr.send();
}

function viewProduct() {
    var id = new XMLSerializer().serializeToString(this.responseXML);
    document.getElementById("editProducts").innerHTML=id;
}

function saveEditProduct(id){
    var record = {
        imagepath: document.getElementById("newImagepath").value,
        name: document.getElementById("newName").value,
        description: document.getElementById("newDescription").value,
    };

    var xhr = new XMLHttpRequest();
    xhr.onload = insertProduct;
    xhr.open("POST", "/shop/product/edit?id="+id);
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.send(JSON.stringify(record));

    document.getElementById("newImagepath").value="";
    document.getElementById("newName").value="";
    document.getElementById("newDescription").value="";
    document.getElementById("imagepath").focus();
}

function refreshShop(r){
    var i = r.parentNode.parentNode.rowIndex;
    document.getElementById("productTable").deleteRow(i);
}

function shopOptionsVis() {

}

