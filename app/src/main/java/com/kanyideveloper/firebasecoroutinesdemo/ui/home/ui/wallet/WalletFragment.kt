package com.kanyideveloper.firebasecoroutinesdemo.ui.home.ui.wallet

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kanyideveloper.firebasecoroutinesdemo.R

class WalletFragment : Fragment() {

    companion object {
        fun newInstance() = WalletFragment()
    }

    private lateinit var viewModel: WalletViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_wallet, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(WalletViewModel::class.java)
        // TODO: Use the ViewModel
    }

}