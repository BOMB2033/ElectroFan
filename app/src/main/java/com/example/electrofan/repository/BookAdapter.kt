package com.example.electrofan.repository

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.electrofan.R
import com.example.electrofan.databinding.ItemProductBinding


class ProductDiffCallback : DiffUtil.ItemCallback<Product>(){
    override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem.uid==newItem.uid
    }

    override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
        return  oldItem == newItem
    }

}
class ProductsViewHolder(private val binding: ItemProductBinding)
    :RecyclerView.ViewHolder(binding.root) {
    fun bind(context: Context,product: Product, listener: ProductsAdapter.Listener, user: User) {
        binding.apply {
            nameProduct.text = product.name
            aboutProduct.text = product.about
            castProduct.text = product.cast.toString()
            if (user.uidProducts.contains(product)){
                buttonBuy.text = "Убрать"
                buttonBuy.backgroundTintList = ContextCompat.getColorStateList(context, R.color.accent2)
            }else{
                    buttonBuy.text = "В корзину"
                buttonBuy.backgroundTintList = ContextCompat.getColorStateList(context, R.color.accent)
            }
            buttonBuy.setOnClickListener {
                listener.addProduct(product)
            }
        }
    }
}

class ProductsAdapter(private val context:Context,private val user:User,
    private val listener: Listener,
):ListAdapter<Product, ProductsViewHolder>(ProductDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductsViewHolder(binding)
    }
    override fun onBindViewHolder(holder: ProductsViewHolder, position:Int){
        val post = getItem(position)
        holder.bind(context,post, listener, user)
    }

    interface Listener{
        fun addProduct(product: Product)
    }
}
