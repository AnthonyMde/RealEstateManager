package com.amamode.realestatemanager.ui.home

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.amamode.realestatemanager.R
import com.amamode.realestatemanager.ui.CurrencyViewModel
import com.amamode.realestatemanager.ui.EstateViewModel
import com.amamode.realestatemanager.ui.SHARED_PREFS_CURRENCY
import com.amamode.realestatemanager.utils.Resource
import com.amamode.realestatemanager.utils.getCurrentCurrencyType
import kotlinx.android.synthetic.main.fragment_estate_list.*
import org.jetbrains.anko.support.v4.defaultSharedPreferences
import org.jetbrains.anko.support.v4.toast
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber

private const val CHANNEL_ID = "estate_creation_notification"

class EstateListFragment : Fragment(R.layout.fragment_estate_list) {
    private val estateViewModel: EstateViewModel by sharedViewModel()
    private val currencyViewModel: CurrencyViewModel by sharedViewModel()
    private var firstTime = true
    private lateinit var adapter: EstateListAdapter
    private val isEuro: Boolean
        get() = defaultSharedPreferences.getBoolean(SHARED_PREFS_CURRENCY, true)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createNotificationChannel()
        val isTablet = resources.getBoolean(R.bool.isTablet)

        estateRV.addItemDecoration(DividerItemDecoration(
            requireContext(),
            LinearLayoutManager.VERTICAL))

        if (isTablet) {
            val tabletDetailNavHost =
                childFragmentManager.findFragmentById(R.id.tablet_detail_nav_container) as NavHostFragment
            displayFullLayout(tabletDetailNavHost)
        } else {
            displaySingleLayout()
            setupToolbar()
            setHasOptionsMenu(true)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        estateViewModel.estateEntityList.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    adapter.setEstateList(it.data)
                }
                is Resource.Loading -> toast("loading")
                is Resource.Error -> Timber.e("Error while loading estate list => ${it.error}")
            }
        })

        currencyViewModel.currencySwitch.observe(viewLifecycleOwner, Observer {
            adapter.currentCurrency = it
            adapter.notifyDataSetChanged()
        })

        estateViewModel.launchNotification.observe(viewLifecycleOwner, Observer {
            sendNotification()
        })
    }

    private fun displaySingleLayout() {
        adapter =
            EstateListAdapter(onEstateClick = { estateId, estateType ->
                val action =
                    EstateListFragmentDirections.goToEstateDetails(
                        estateId,
                        estateType
                    )
                findNavController().navigate(action)
            }, context = context, currentCurrency = getCurrentCurrencyType(context))

        estateRV.adapter = adapter
        addEstateFab.setOnClickListener {
            estateViewModel.clearFormerCreationData()
            findNavController().navigate(R.id.goToEstateCreation)
        }
    }

    private fun displayFullLayout(tabletDetailNavHost: NavHostFragment) {
        adapter =
            EstateListAdapter(onEstateClick = { estateId, estateType ->
                if (firstTime) {
                    firstTime = false
                    val action =
                        EmptyDetailFragmentDirections.emptyFragmentGoToEstateDetails(
                            estateId,
                            estateType
                        )
                    tabletDetailNavHost.navController.navigate(action)
                } else {
                    val action =
                        EstateListFragmentDirections.goToEstateDetails(
                            estateId,
                            estateType
                        )
                    tabletDetailNavHost.navController.navigate(action)
                }
            }, context = context, currentCurrency = getCurrentCurrencyType(context))
        estateRV.adapter = adapter
    }

    private fun sendNotification() {
        val ctx = context ?: return
        val builder = NotificationCompat.Builder(ctx, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_house_black_24)
            .setContentTitle(getString(R.string.notification_estate_creation_title))
            .setContentText(getString(R.string.notification_estate_creation_content))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        with(NotificationManagerCompat.from(ctx)) {
            notify(1, builder.build())
        }
    }

    /**
     * Android version above or egal to Oreo needs a notification channel to be configured
     * to receive notifications properly.
     */
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.notification_channel_estate_name)
            val description = getString(R.string.notification_channel_estate_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            channel.description = description
            // Register the channel with the system. We can't change notification behaviors after.
            val notificationManager = context?.getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
        }
    }

    /* ONLY FOR MOBILE */
    private fun setupToolbar() {
        (activity as? AppCompatActivity)?.setSupportActionBar(estateListToolbar as Toolbar)
        (activity as? AppCompatActivity)?.apply {
            title = getString(R.string.estate_list_toolbar_title)
        }
    }

    /* ONLY FOR MOBILE */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.main_menu_mobile, menu)
        menu.findItem(R.id.switch_currency)
            ?.setIcon(if (isEuro) R.drawable.ic_euro_black_24 else R.drawable.ic_dollar_black_24)
    }
}
