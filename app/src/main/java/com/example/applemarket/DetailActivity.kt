package com.example.applemarket

import ProductViewModel
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.applemarket.databinding.ActivityDetailBinding
import com.example.applemarket.product.ProductModel
import com.google.android.material.snackbar.Snackbar
import java.text.NumberFormat
import java.util.Locale

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var viewModel: ProductViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    private fun initView() {
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(ProductViewModel::class.java)

        val product = intent.getParcelableExtra("product") as? ProductModel
        val position = intent.getIntExtra("position", -1)

        val productList = viewModel.productList.value.orEmpty().toMutableList()

        val resourceId = resources.getIdentifier(
            product?.imgFile, "drawable", packageName
        )
        val drawable = resources.getDrawable(resourceId, null)

        binding.imgItem.setImageDrawable(drawable)
        binding.textSeller.text = product!!.seller
        binding.textLocate.text = product.locate
        binding.textTitle.text = product.title
        binding.textInfo.text = product.info

        binding.imgHeart.setOnClickListener {
            val itemIndex = productList.indexOfFirst { it.id == product.id }

            if (itemIndex != -1) {
                if (product.myFavorite) {
                    binding.imgHeart.setImageResource(R.drawable.heart_icon)
                    Snackbar.make(binding.root, "찜 목록에 제거되었습니다.", Snackbar.LENGTH_SHORT).show()
                } else {
                    binding.imgHeart.setImageResource(R.drawable.heart_fill_icon)
                    Snackbar.make(binding.root, "찜 목록에 추가되었습니다.", Snackbar.LENGTH_SHORT).show()
                }

                productList[itemIndex].myFavorite = !productList[itemIndex].myFavorite

                viewModel.updateProductList(productList)
            }
        }


        val formattedPrice = NumberFormat.getNumberInstance(Locale.US).format(product.price)
        binding.textPrice.text = formattedPrice + "원"
    }
}