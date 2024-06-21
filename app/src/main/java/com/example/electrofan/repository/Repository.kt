package com.example.electrofan.repository

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class RepositoryInMemoryImpl {

    private var databaseUsersReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("users")
    private var databaseProductsReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("products")
    private val uid:String = Firebase.auth.currentUser!!.uid

    var dataClass = DataClass(
        emptyList(),
        emptyList(),
    )
    private val data = MutableLiveData(dataClass)

    fun getAll() = data

    fun loadUser() {
        val listener = object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataClass.users = emptyList()
                dataSnapshot.children.mapNotNull { it.getValue(User::class.java) }.forEach{
                    dataClass.users = dataClass.users.plus(it)
                }
                data.value = dataClass
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
            }
        }
        databaseUsersReference.addValueEventListener(listener)
    }

    fun addUser(email:String) {
        dataClass.users = dataClass.users.plus(User(uid = uid, email = email))
        data.value = dataClass
        databaseUsersReference.child(uid).removeValue()
        databaseUsersReference.child(uid).setValue(User(uid = uid))
    }
    fun addProduct(product: Product) {
            val newUid = databaseProductsReference.push().key!!
            dataClass.products = dataClass.products.plus(product.copy(uid = newUid))
            data.value = dataClass
            databaseProductsReference.child(newUid).setValue(product.copy(uid = newUid))
    }
    fun addProductUser(product: Product) {
        dataClass.users.forEach { user ->
            if (user.uid == uid){
                if (getCurrentUser().uidProducts.contains(product))
                    user.uidProducts = user.uidProducts.filter { it.uid != product.uid }
                else {
                    user.uidProducts =  user.uidProducts.plus(product)
                    data.value = dataClass
                }
                databaseUsersReference.child(uid).setValue(getCurrentUser())
            }
        }

    }
    fun getCurrentUser():User{
        dataClass.users.forEach {
            if (it.uid == uid) return it
        }
        return User()
    }

    fun getProduct(uidProduct: String): Product {
        dataClass.products.forEach {
            if (it.uid == uidProduct) return it
        }
        return Product()
    }

    fun loadProducts() {
        val listener = object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataClass.products = emptyList()
                dataSnapshot.children.mapNotNull { it.getValue(Product::class.java) }.forEach{
                    dataClass.products = dataClass.products.plus(it)
                }
                data.value = dataClass
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
            }
        }
        databaseProductsReference.addValueEventListener(listener)
        /*databaseProductsReference.setValue(
            listOf(
                Product("1","Видеокарта","",3000),
                Product("2","Процессор","",5000),
                Product("3","Монитор","",12000),
                Product("4","Мышка","",220),
                Product("5","Клавиатура","",530),
                Product("6","Ноутбук","",70000),
                Product("7","Коврик","",500),
                Product("8","Подставка","",700),
                Product("9","Чайник","",99999),
            )
        )*/
    }


}


class DataViewModel : ViewModel() {
    private val repository = RepositoryInMemoryImpl()
    val data = repository.getAll()
    val uid:String = Firebase.auth.currentUser!!.uid
    fun getUser() = repository.getCurrentUser()
    fun addUser(email:String) = repository.addUser(email)
    fun loadUsers() = repository.loadUser()
    fun loadProducts() = repository.loadProducts()
    fun addProduct(product: Product) = repository.addProduct(product)
    fun addProductUser(product: Product) = repository.addProductUser(product)
    fun getBook(uidBook: String) = repository.getProduct(uidBook)
}