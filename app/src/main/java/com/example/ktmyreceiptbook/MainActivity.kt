package com.example.ktmyreceiptbook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ktmyreceiptbook.fragments.LISTING_FRAGMENT_TAG
import com.example.ktmyreceiptbook.fragments.ReceiptListingFragment

class MainActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, ReceiptListingFragment(), LISTING_FRAGMENT_TAG)
            .commitNow()
    }
}
