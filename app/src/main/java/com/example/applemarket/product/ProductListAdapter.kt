package com.example.applemarket.product

import ProductViewModel
import android.content.Intent
import android.os.Debug
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.example.applemarket.DetailActivity
import com.example.applemarket.R
import com.example.applemarket.databinding.ProductItemBinding
import java.text.NumberFormat
import java.util.Locale

class ProductListAdapter(private val viewModel: ProductViewModel) :
    RecyclerView.Adapter<ProductListAdapter.ViewHolder>() {

    private var productList: List<ProductModel> = emptyList()

    // LiveData를 관찰하고 데이터가 변경될 때 RecyclerView를 업데이트
    private val productListObserver = Observer<List<ProductModel>> { newProductList ->
        productList = newProductList
        notifyDataSetChanged()
    }

    init {
        viewModel.productList.observeForever(productListObserver)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ProductItemBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = viewModel.productList.value?.get(position)
        item?.let {
            holder.bind(it)
        }
    }

    override fun getItemCount(): Int {
        return viewModel.productList.value?.size ?: 0
    }

    inner class ViewHolder(
        private val binding: ProductItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ProductModel) {
            with(binding) {
                textTitle.text = item.title
                textTitle.maxLines = 2
                textTitle.ellipsize = TextUtils.TruncateAt.END

                val resourceId = itemView.context.resources.getIdentifier(
                    item.imgFile, "drawable", itemView.context.packageName
                )
                imgProduct.setImageResource(resourceId)
                imgProduct.clipToOutline = true

                textLocate.text = item.locate
                val formattedPrice = NumberFormat.getNumberInstance(Locale.US).format(item.price)
                textPrice.text = formattedPrice + "원"
                textChat.text = item.chat.toString()
                textHeart.text = item.heart.toString()


                if (item.myFavorite) {
                    imgHeart.setImageResource(R.drawable.heart_fill_icon)
                } else {
                    imgHeart.setImageResource(R.drawable.heart_icon)
                }

                // 세부 페이지 이동...
                layoutItem.setOnClickListener {
                    val intent = Intent(itemView.context, DetailActivity::class.java)
                    intent.putExtra("product", item)
                    intent.putExtra("position", adapterPosition)
                    itemView.context.startActivity(intent)
                }

                layoutItem.setOnLongClickListener {
                    val dialogView =
                        LayoutInflater.from(itemView.context).inflate(R.layout.delete_dialog, null)
                    val builder = AlertDialog.Builder(itemView.context)
                    builder.setView(dialogView)
                    val dialog = builder.create()

                    val btnOk = dialogView.findViewById<Button>(R.id.btn_ok)
                    val btnCancel = dialogView.findViewById<Button>(R.id.btn_cancle)

                    btnOk.setOnClickListener {
                        viewModel.removeItem(adapterPosition)
                        notifyDataSetChanged()
                        dialog.dismiss()
                    }

                    btnCancel.setOnClickListener {
                        dialog.dismiss()
                    }

                    dialog.show()
                    true
                }
            }
        }
    }

}
