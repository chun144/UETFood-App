package com.chun.uetfood.ui.main

import android.content.Intent
import android.os.Bundle
import com.chun.uetfood.R
import com.chun.uetfood.common.SharedPreferenceCommon
import com.chun.uetfood.model.order.OrderComposition
import com.chun.uetfood.ui.base.activity.BaseActivity
import com.chun.uetfood.ui.main.fragment.*
import com.chun.uetfood.ui.start.StartActivity

class MainActivity : BaseActivity() {
    var listOrderComposition = mutableListOf<OrderComposition>()
    var phone: String = ""
    var address: String = ""
    var note: String = ""
    var totalPriceOrder: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction()
            .add(R.id.main, HomeFragment(), HomeFragment::class.java.name)
            .commit()
    }

    fun openFoodAdminFragment() {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.open_to_left, R.anim.exit_to_left,
                R.anim.open_to_right, R.anim.exit_to_right)
            .replace(R.id.main, FoodAdminFragment(), FoodAdminFragment::class.java.name)
            .commit()
    }

    fun openFoodUserFragment() {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.open_to_left, R.anim.exit_to_left,
                R.anim.open_to_right, R.anim.exit_to_right)
            .replace(R.id.main, FoodUserFragment(), FoodUserFragment::class.java.name)
            .commit()
    }

    fun openFoodOrderFragment(checkCart: Boolean) {
        val foodOrderFragment = FoodOrderFragment()
        val bundle = Bundle()
        bundle.putBoolean("checkCart", checkCart)
        foodOrderFragment.arguments = bundle

        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.open_to_left, R.anim.exit_to_left,
                R.anim.open_to_right, R.anim.exit_to_right)
            .replace(R.id.main, foodOrderFragment, FoodOrderFragment::class.java.name)
            .commit()
    }

    fun openCartFragment() {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.open_to_left, R.anim.exit_to_left,
                R.anim.open_to_right, R.anim.exit_to_right)
            .replace(R.id.main, CartFragment(), CartFragment::class.java.name)
            .commit()
    }

    fun openOrderFragment() {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.open_to_left, R.anim.exit_to_left,
                R.anim.open_to_right, R.anim.exit_to_right)
            .replace(R.id.main, OrderFragment(), OrderFragment::class.java.name)
            .commit()
    }

    fun openOrderShipperFragment() {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.open_to_left, R.anim.exit_to_left,
                R.anim.open_to_right, R.anim.exit_to_right)
            .replace(R.id.main, OrderShipperFragment(), OrderShipperFragment::class.java.name)
            .commit()
    }

    fun openOrderDetailFragment(orderId: Int, checkShip: Boolean) {
        val orderDetailFragment = OrderDetailFragment()
        val bundle = Bundle()
        bundle.putInt("orderId", orderId)
        bundle.putBoolean("checkShip", checkShip)
        orderDetailFragment.arguments = bundle

        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.open_to_left, R.anim.exit_to_left,
                R.anim.open_to_right, R.anim.exit_to_right)
            .replace(R.id.main, orderDetailFragment, OrderDetailFragment::class.java.name)
            .commit()
    }

    fun openOrderDetailShipperFragment(orderId: Int) {
        val orderDetailShipperFragment = OrderDetailShipperFragment()
        val bundle = Bundle()
        bundle.putInt("orderId", orderId)
        orderDetailShipperFragment.arguments = bundle

        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.open_to_left, R.anim.exit_to_left,
                R.anim.open_to_right, R.anim.exit_to_right)
            .replace(R.id.main, orderDetailShipperFragment, OrderDetailShipperFragment::class.java.name)
            .commit()
    }

    fun openCommunicateFragment() {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.open_to_left, R.anim.exit_to_left,
                R.anim.open_to_right, R.anim.exit_to_right)
            .replace(R.id.main, CommunicateFragment(), CommunicateFragment::class.java.name)
            .commit()
    }

    fun openQuestionFragment(questionId: Int) {
        val questionFragment = QuestionFragment()
        val bundle = Bundle()
        bundle.putInt("questionId", questionId)
        questionFragment.arguments = bundle

        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.open_to_left, R.anim.exit_to_left,
                R.anim.open_to_right, R.anim.exit_to_right)
            .replace(R.id.main, questionFragment, QuestionFragment::class.java.name)
            .commit()
    }

    fun openFeedbackFragment() {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.open_to_left, R.anim.exit_to_left,
                R.anim.open_to_right, R.anim.exit_to_right)
            .replace(R.id.main, FeedbackFragment(), FeedbackFragment::class.java.name)
            .commit()
    }

    fun openHomeFragment() {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.open_to_left, R.anim.exit_to_left,
                R.anim.open_to_right, R.anim.exit_to_right)
            .replace(R.id.main, HomeFragment(), HomeFragment::class.java.name)
            .commit()
    }

    fun openFoodEditFragment(id: Int?, name: String?, image: String?, description: String?, price: Int?) {
        val foodEditFragment = FoodEditFragment()
        if (id != null && name != null && image != null && description != null && price != null) {
            val bundle = Bundle()
            bundle.putInt("id", id)
            bundle.putString("name", name)
            bundle.putString("image", image)
            bundle.putString("description", description)
            bundle.putInt("price", price)
            foodEditFragment.arguments = bundle
        }
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.open_to_left, R.anim.exit_to_left,
                R.anim.open_to_right, R.anim.exit_to_right)
            .replace(R.id.main, foodEditFragment, FoodEditFragment::class.java.name)
            .commit()
    }

    fun openStart() {
        SharedPreferenceCommon.saveUserToken(this, "")
        val intent = Intent()
        intent.setClass(this, StartActivity::class.java)
        intent.putExtra("TYPE","OPEN_LOGIN")
        startActivity(intent)
        finish()
    }
}