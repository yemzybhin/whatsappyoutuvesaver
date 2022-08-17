package ade.yemi.youtubewhatsappsaver.fragments

import ade.yemi.moreapps.Network.RetrofitInterface
import ade.yemi.moreapps.Network.RetrofitInterface1
import ade.yemi.moreapps.models.AllAppDetails
import ade.yemi.moreapps.models.AppContent
import ade.yemi.youtubewhatsappsaver.Activities.Activity2
import ade.yemi.youtubewhatsappsaver.Activities.MainActivity
import ade.yemi.youtubewhatsappsaver.Adapters.MoreAppsAdapter
import ade.yemi.youtubewhatsappsaver.Data.Preferencestuff
import ade.yemi.youtubewhatsappsaver.R
import ade.yemi.youtubewhatsappsaver.Ultilities.*
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.billingclient.api.*
import com.bumptech.glide.Glide
import com.facebook.ads.Ad
import com.facebook.ads.AudienceNetworkAds
import com.facebook.ads.RewardedVideoAd
import com.facebook.ads.RewardedVideoAdListener
import com.google.android.gms.ads.*
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.unity3d.mediation.*
import com.unity3d.mediation.errors.LoadError
import com.unity3d.mediation.errors.SdkInitializationError
import com.unity3d.mediation.errors.ShowError
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

const val base64Key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA6CfO87Dd5XZ1qFEte8Yx3iDG66swL5P/mawmeb/cNSFv2kO3r1GHae/TnHhnAcSdQaHRaV7bEFsJg/rp11UzshKQtu2WobCj49MfEHgrK12dOGLF2Tfbc5RMloCZu+ciSDZeuAhcnOBrCs1uNe6uqNNpmqTwHtH2MvSCxMDK/lzMH7DJ2uBvMup4OloGcIf8kiubMw86oG/6VdCyB+qzv3VRobDu+ov/o4yvidGtR8Hy8Ud5FNf43XomPLcT6O4/1BiwSP3KKDkZ0hnMA3jd1aGMo5RLX9ABaB32yTwiPTiS2xFnjRoyf0XofVgS8Bw4uSm1Zhd7IwuMBsscxiUJ9wIDAQAB"
const val AD_UNIT_ID = "ca-app-pub-2144911759176506/5399035215"
const val PREF_FILE = "MyPref"
const val PURCHASE_KEY = "consumable"
const val PRODUCT_ID = "consumable1"

const val UNITY_GAME_ID = "4889511"
const val UNITYREWARDED_ID = "Rewarded_Android"
const val FACEBOOK_AD_UNIT = "429889565196930_429890685196818"   //App id -> 1217060465749626_1217125615743111

class AboutsPage : Fragment() , PurchasesUpdatedListener {
    private var billingClient: BillingClient? = null
    private lateinit var manager: RecyclerView.LayoutManager
    private lateinit var  myAdapter: RecyclerView.Adapter<*>

    //private var rewardedVideoAd: RewardedVideoAd? = null

    private var mIsLoading = false
    private var mRewardedAd: RewardedAd? = null
    private var adcheck = false
    private var admobvisible = false

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        var initializationConfiguration = InitializationConfiguration.builder()
            .setGameId(UNITY_GAME_ID)
            .setInitializationListener(object : IInitializationListener {
                override fun onInitializationComplete() {
                }

                override fun onInitializationFailed(p0: SdkInitializationError?, p1: String?) {
                }
            }).build()

        UnityMediation.initialize(initializationConfiguration)
        MobileAds.initialize(requireContext()) {}
        AudienceNetworkAds.initialize(requireContext())

        var view = inflater.inflate(R.layout.fragment_abouts_page, container, false)
        var recyclerView = view.findViewById<RecyclerView>(R.id.rv_moreapps)
        var retry = view.findViewById<TextView>(R.id.tv_retry)
        var loader = view.findViewById<ProgressBar>(R.id.lt_loader)
        var rateus = view.findViewById<Button>(R.id.btn_rateus)
        var adprogress = view.findViewById<ProgressBar>(R.id.adprogress)
        var cancel = view.findViewById<CardView>(R.id.pastquestionscancel)
        var adspace = view.findViewById<CardView>(R.id.adspace)
        var adimage = view.findViewById<ImageView>(R.id.adimage)
        var loadad = view.findViewById<Button>(R.id.Loadadd)
        var admessage = view.findViewById<TextView>(R.id.message)
        var ad2 = view.findViewById<Button>(R.id.Loadadd2)

        var admobspace = view.findViewById<CardView>(R.id.admobadspace)
        var facebookspace = view.findViewById<CardView>(R.id.facebookadspace)
        var unityspace = view.findViewById<CardView>(R.id.unityadspace)
//        appLovinSpace = view.findViewById(R.id.applovinSpace)

        admobspace.visibility = View.GONE
        facebookspace.visibility = View.GONE
        unityspace.visibility = View.GONE

        ad2.setOnClickListener {
            ad2.clicking()
            (activity as Activity2).replacefragment(AllAds())
        }

        var buypoints = view.findViewById<Button>(R.id.buy300Points)
        billingClient = BillingClient.newBuilder(requireContext()).enablePendingPurchases().setListener(this).build()
        billingClient!!.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    val queryPurchase = billingClient!!.queryPurchases(BillingClient.SkuType.INAPP)
                    val queryPurchases = queryPurchase.purchasesList
                    if (queryPurchases != null && queryPurchases.size > 0) {
                        handlePurchases(queryPurchases)
                    }
                }
            }

            override fun onBillingServiceDisconnected() {}
        })


        buypoints.setOnClickListener {
            buypoints.clicking()
            confirmtopurchase()
        }

        loadad.text = "Load Ad: +20 Points"


        getdata1(adspace, adimage, admessage)
        adspace.visibility = View.GONE
        adprogress.visibility = View.GONE
        loader.visibility = View.VISIBLE
        retry.visibility = View.GONE


        //initializing facebook ads
       // AudienceNetworkAds.initialize(requireContext())

        manager = LinearLayoutManager(requireContext())
        getdata(loader, retry, recyclerView)
        retry.setOnClickListener {
            retry.clicking()
            getdata(loader, retry, recyclerView)
        }
        rateus.setOnClickListener {
            rateus.clicking()
            val uriUri = Uri.parse("https://play.google.com/store/apps/details?id=ade.yemi.youtubewhatsappsaver")
            val launchBrowser = Intent(Intent.ACTION_VIEW, uriUri)
            startActivity(launchBrowser)
        }
        loadad.setOnClickListener {
            loadad.clicking()
            if(unityspace.visibility == View.VISIBLE || admobspace.visibility == View.VISIBLE || facebookspace.visibility == View.VISIBLE){
                Toast.makeText(
                    requireContext(),
                    "There is still an AD yet to be viewed",
                    Toast.LENGTH_SHORT
                ).show()
            }else{
                adprogress.visibility = View.VISIBLE
                Toast.makeText(requireContext(), "Ad is loading. Please Wait", Toast.LENGTH_LONG).show()
                unityadscon(adprogress, unityspace, facebookspace)
                loadRewardedAdAdmob(adprogress, admobspace)
//                AppLovinrewardedAd.loadAd()
            }
        }
        cancel.setOnClickListener {
            cancel.clicking()
            Handler().postDelayed({
                startActivity(Intent(requireContext(), MainActivity::class.java))
            }, 0)
            (activity as Activity2).loading()
        }
        return view
    }
    private fun unityadscon(adprogress: ProgressBar, cardView: CardView, cardView2: CardView){
        var rewarded = com.unity3d.mediation.RewardedAd(requireActivity(), UNITYREWARDED_ID)
        rewarded.load(object : IRewardedAdLoadListener {
            override fun onRewardedLoaded(p0: com.unity3d.mediation.RewardedAd?) {
                cardView.visibility = View.VISIBLE
                adprogress.visibility = View.GONE
                Toast.makeText(requireContext(), "Ad is loaded. Click to view Ad.",Toast.LENGTH_SHORT).show()
                var animation = AnimationUtils.loadAnimation(requireContext(), R.anim.adscale)
                cardView.startAnimation(animation)
                cardView.setOnClickListener {
                    cardView.clicking()
                    rewarded.show(object : IRewardedAdShowListener {
                        override fun onRewardedShowed(p0: com.unity3d.mediation.RewardedAd?) {
                            //Toast.makeText(requireActivity(), "3", Toast.LENGTH_SHORT).show()
                        }

                        override fun onRewardedClicked(p0: com.unity3d.mediation.RewardedAd?) {
                            //   Toast.makeText(requireActivity(), "4", Toast.LENGTH_SHORT).show()
                        }

                        override fun onRewardedClosed(p0: com.unity3d.mediation.RewardedAd?) {
                            //Toast.makeText(requireActivity(), "5", Toast.LENGTH_SHORT).show()
                        }

                        override fun onRewardedFailedShow(
                            p0: com.unity3d.mediation.RewardedAd?,
                            p1: ShowError?,
                            p2: String?
                        ) {
                            //   Toast.makeText(requireActivity(), "6 $p1 $p2", Toast.LENGTH_SHORT).show()
                        }

                        override fun onUserRewarded(
                            p0: com.unity3d.mediation.RewardedAd?,
                            p1: IReward?
                        ) {
                            // Toast.makeText(requireActivity(), "7", Toast.LENGTH_SHORT).show()
                            var prefereceStuffs = Preferencestuff(requireContext())
                            var newpoints = prefereceStuffs.getPoint() + 20
                            prefereceStuffs.setPoint(newpoints)
                            //  Toast.makeText(requireContext(), "20 Points Added", Toast.LENGTH_LONG).show()
                        }
                    })
                    Handler().postDelayed({
                        cardView.visibility = View.GONE
                    }, 2000)

                }
            }

            override fun onRewardedFailedLoad(
                p0: com.unity3d.mediation.RewardedAd?,
                p1: LoadError?,
                p2: String?
            ) {
                  Toast.makeText(requireContext(), "${p1}, $p2", Toast.LENGTH_SHORT).show()
                if (admobvisible !== true) {
                    loadfacebookad(adprogress, cardView2)
                    adprogress.visibility = View.VISIBLE
                    cardView.visibility = View.GONE
                } else {
                    adprogress.visibility = View.GONE
                }
            }

        })
    }
    private fun loadfacebookad(progressBar: ProgressBar, cardView: CardView){
        var rewardedVideoAd = RewardedVideoAd(requireContext(), FACEBOOK_AD_UNIT)

        val rewardedVideoAdListener: RewardedVideoAdListener = object : RewardedVideoAdListener {
            override fun onError(ad: Ad?, error: com.facebook.ads.AdError) {
                // Rewarded video ad failed to load
                Toast.makeText(
                    requireContext(),
                    "Rewarded video ad failed to load: ${error.errorMessage}",
                    Toast.LENGTH_SHORT
                ).show()
                progressBar.visibility = View.GONE
                // Log.e(TAG, "Rewarded video ad failed to load: " + error.getErrorMessage())
            }
            override fun onAdLoaded(ad: Ad) {
                progressBar.visibility = View.GONE
                cardView.visibility = View.VISIBLE
                var animation = AnimationUtils.loadAnimation(requireContext(), R.anim.adscale)
                cardView.startAnimation(animation)
                cardView.setOnClickListener {
                    cardView.clicking()
                    rewardedVideoAd.show()
                    cardView.visibility = View.GONE
                }
            }

            override fun onAdClicked(ad: Ad) {
            }
            override fun onLoggingImpression(ad: Ad) {

            }

            override fun onRewardedVideoCompleted() {
                var prefereceStuffs = Preferencestuff(requireContext())
                var newpoints = prefereceStuffs.getPoint() + 20
                prefereceStuffs.setPoint(newpoints)
                Toast.makeText(requireContext(), "20 Points Added", Toast.LENGTH_LONG).show()
            }

            override fun onRewardedVideoClosed() {
                // The Rewarded Video ad was closed - this can occur during the video
                // by closing the app, or closing the end card.
                //Toast.makeText(requireContext(), "Rewarded video ad closed!", Toast.LENGTH_SHORT).show()
                //Log.d(TAG, "Rewarded video ad closed!")
            }
        }
        rewardedVideoAd!!.loadAd(
            rewardedVideoAd!!.buildLoadAdConfig()
                .withAdListener(rewardedVideoAdListener)
                .build()
        )
    }

    private fun confirmtopurchase(){
        var view = Dialog(requireContext())
        view.setCancelable(true)
        view.setContentView(R.layout.paymentconfirm)
        view.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        view.show()

        var confirm = view.findViewById<Button>(R.id.buypoints)
        confirm.setOnClickListener {
            confirm.clicking()
            consume()
            view.dismiss()
        }
    }
    private fun confirmtopurchase2(){
        var view = Dialog(requireContext())
        view.setCancelable(true)
        view.setContentView(R.layout.paymentconfirm2)
        view.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        view.show()
    }
    fun consume() {
        //check if service is already connected
        if (billingClient!!.isReady) {
            initiatePurchase()
        } else {
            billingClient = BillingClient.newBuilder(requireContext()).enablePendingPurchases().setListener(this).build()
            billingClient!!.startConnection(object : BillingClientStateListener {
                override fun onBillingSetupFinished(billingResult: BillingResult) {
                    if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                        initiatePurchase()
                    } else {
                        Toast.makeText(requireContext(), "Error " + billingResult.debugMessage, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onBillingServiceDisconnected() {}
            })
        }
    }
    private fun initiatePurchase() {
        val skuList: MutableList<String> = ArrayList()
        skuList.add(PRODUCT_ID)
        val params = SkuDetailsParams.newBuilder()
        params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP)
        billingClient!!.querySkuDetailsAsync(params.build()
        )
        { billingResult, skuDetailsList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                if (skuDetailsList != null && skuDetailsList.size > 0) {
                    val flowParams = BillingFlowParams.newBuilder()
                            .setSkuDetails(skuDetailsList[0])
                            .build()
                    billingClient!!.launchBillingFlow(requireActivity(), flowParams)
                }
                else {
                    //try to add item/product id "consumable" inside managed product in google play console
                    Toast.makeText(requireContext(), "Purchase Item not Found", Toast.LENGTH_SHORT).show()
                }
            }
            else {
                Toast.makeText(requireContext(), " Error " + billingResult.debugMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onPurchasesUpdated(billingResult: BillingResult, purchases: List<Purchase>?) {
        //if item newly purchased
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            handlePurchases(purchases)
        } else if (billingResult.responseCode == BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED) {
            val queryAlreadyPurchasesResult = billingClient!!.queryPurchases(BillingClient.SkuType.INAPP)
            val alreadyPurchases = queryAlreadyPurchasesResult.purchasesList
            alreadyPurchases?.let { handlePurchases(it) }
        } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
            Toast.makeText(requireContext(), "Purchase Canceled", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Error " + billingResult.debugMessage, Toast.LENGTH_SHORT).show()
        }
    }
    fun handlePurchases(purchases: List<Purchase>) {
        for (purchase in purchases) {
            //if item is purchased
            if (PRODUCT_ID == purchase.sku && purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
                if (!verifyValidSignature(purchase.originalJson, purchase.signature)) {
                    // Invalid purchase
                    // show error to user
                    Toast.makeText(requireContext(), "Error : Invalid Purchase", Toast.LENGTH_SHORT).show()
                    return
                }
                // else purchase is valid
                //if item is purchased and not consumed
                if (!purchase.isAcknowledged) {
                    val consumeParams = ConsumeParams.newBuilder()
                            .setPurchaseToken(purchase.purchaseToken)
                            .build()
                    billingClient!!.consumeAsync(consumeParams, consumeListener)
                }
            } else if (PRODUCT_ID == purchase.sku && purchase.purchaseState == Purchase.PurchaseState.PENDING) {
                Toast.makeText(requireContext(), "Purchase is Pending. Please complete Transaction", Toast.LENGTH_SHORT).show()
            } else if (PRODUCT_ID == purchase.sku && purchase.purchaseState == Purchase.PurchaseState.UNSPECIFIED_STATE) {
                Toast.makeText(requireContext(), "Purchase Status Unknown", Toast.LENGTH_SHORT).show()
            }
        }
    }
    var consumeListener = ConsumeResponseListener { billingResult, purchaseToken ->
        var prefereceStuffs = Preferencestuff(requireContext())
        var currentpoints = prefereceStuffs.getPoint()
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
            prefereceStuffs.setPoint(currentpoints + 300)
            confirmtopurchase2()
        }
    }
    /**
     * Verifies that the purchase was signed correctly for this developer's public key.
     *
     * Note: It's strongly recommended to perform such check on your backend since hackers can
     * replace this method with "constant true" if they decompile/rebuild your app.
     *
     */
    private fun verifyValidSignature(signedData: String, signature: String): Boolean {
        return try {
                    ade.yemi.youtubewhatsappsaver.Ultilities.Security.verifyPurchase(base64Key, signedData, signature)
        } catch (e: IOException) {
            false
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        if (billingClient != null) {
            billingClient!!.endConnection()
        }
    }
    private fun getdata(loader: ProgressBar, retry: TextView, recyclerView: RecyclerView){
        var rf = Retrofit.Builder()
            .baseUrl(RetrofitInterface.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        var API = rf.create(RetrofitInterface::class.java)
        var call =API.post


        call?.enqueue(object : Callback<AllAppDetails?> {
            override fun onResponse(
                call: Call<AllAppDetails?>,
                response: Response<AllAppDetails?>
            ) {
                var appDetails: AllAppDetails? = response.body() as AllAppDetails
                var applist = appDetails?.apps
                recyclerView.apply {
                    myAdapter = MoreAppsAdapter(applist!!)
                    layoutManager = manager
                    adapter = myAdapter
                }
                retry.visibility = View.GONE
                loader.visibility = View.GONE

                var touse = AppToJsonString(appDetails!!)
                Preferencestuff(requireContext()).setloacalApps(touse)
            }

            override fun onFailure(call: Call<AllAppDetails?>, t: Throwable) {

                var touse = Preferencestuff(requireContext()).getlocalApps()
                if (touse != ""){
                    var appContent = generateApps(requireContext(), touse!!)
                    var applist = appContent?.apps
                    recyclerView.apply {
                        myAdapter = MoreAppsAdapter(applist!!)
                        layoutManager = manager
                        adapter = myAdapter
                    }
                }else{
                    Toast.makeText(requireContext(), "Could not load Apps. Check connection", Toast.LENGTH_SHORT).show()
                    retry.visibility = View.VISIBLE
                }
                loader.visibility = View.GONE

            }
        })
    }
    private fun loadRewardedAdAdmob(progressBar: ProgressBar, admobspace: CardView) {

        if (mRewardedAd == null) {
            mIsLoading = true
            var adRequest = AdRequest.Builder().build()
            RewardedAd.load(
                requireContext(), AD_UNIT_ID, adRequest,
                object : RewardedAdLoadCallback() {
                    override fun onAdFailedToLoad(adError: LoadAdError) {
//                            Toast.makeText(requireContext(), "Ad failed to load, check your connection.", Toast.LENGTH_SHORT).show()
                        //loadfacebookad(progressBar)
                        admobvisible = false
                        //progressBar.visibility = View.GONE
                        mIsLoading = false
                        mRewardedAd = null
                    }

                    val animation = AnimationUtils.loadAnimation(context,R.anim.adscale)
                    override fun onAdLoaded(rewardedAd: RewardedAd) {
                        Toast.makeText(
                            requireContext(),
                            "Ad is loaded, click the button to view",
                            Toast.LENGTH_SHORT
                        ).show()
                        admobspace.visibility = View.VISIBLE
                        admobvisible = true
                        admobspace.startAnimation(animation)
                        //progressBar.visibility = View.GONE
                        mRewardedAd = rewardedAd
                        mIsLoading = false
                        admobspace.setOnClickListener {
                            admobspace.clicking()
                            showRewardedVideoAdmob(progressBar, admobspace)
                            admobspace.visibility = View.GONE
                        }
                    }
                }
            )
        }
    }
    private fun showRewardedVideoAdmob(progressBar: ProgressBar, admobss: CardView) {
        admobss.visibility = View.GONE
        if (mRewardedAd != null) {
            mRewardedAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    // Don't forget to set the ad reference to null so you
                    // don't show the ad a second time.
                    mRewardedAd = null
                    //loadRewardedAd(progressBar, load)
                }
                override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
                    Toast.makeText(
                        requireContext(),
                        "Ad failed to show, Check Connection",
                        Toast.LENGTH_SHORT
                    ).show()
                    mRewardedAd = null
                }
                override fun onAdShowedFullScreenContent() {
                    // Called when ad is dismissed.
                }
            }
            mRewardedAd?.show(this.requireActivity(), OnUserEarnedRewardListener() {
                var prefereceStuffs = Preferencestuff(requireContext())
                var newpoints = prefereceStuffs.getPoint() + 20
                prefereceStuffs.setPoint(newpoints)
                Toast.makeText(requireContext(), "20 Points Added", Toast.LENGTH_LONG).show()
            })
        }
    }

    private fun showRewardedVideo(progressBar: ProgressBar, load: Button) {
        if (mRewardedAd != null) {
            mRewardedAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    // Don't forget to set the ad reference to null so you
                    // don't show the ad a second time.
                    mRewardedAd = null
                    //loadRewardedAd(progressBar, load)
                }
                override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
                    Toast.makeText(requireContext(), "Ad failed to load, Check Connection", Toast.LENGTH_SHORT).show()
                    mRewardedAd = null
                }

                override fun onAdShowedFullScreenContent() {
                    // Called when ad is dismissed.
                }
            }
                mRewardedAd?.show(this.requireActivity(), OnUserEarnedRewardListener() {
                    var prefereceStuffs = Preferencestuff(requireContext())
                    var newpoints = prefereceStuffs.getPoint() + 20
                    prefereceStuffs.setPoint(newpoints)
                    Toast.makeText(requireContext(), "20 Points Added", Toast.LENGTH_LONG).show()
                })
        }
    }
    private fun loadRewardedAd(progressBar: ProgressBar, load: Button) {
        progressBar.visibility = View.VISIBLE
        if (mRewardedAd == null) {
            mIsLoading = true
            var adRequest = AdRequest.Builder().build()

            RewardedAd.load(
                    requireContext(), AD_UNIT_ID, adRequest,
                    object : RewardedAdLoadCallback() {
                        override fun onAdFailedToLoad(adError: LoadAdError) {
                            Toast.makeText(requireContext(), "Ad failed to load, check your connection.", Toast.LENGTH_SHORT).show()
                            //loadfacebookad(progressBar)
                            progressBar.visibility = View.GONE
                            mIsLoading = false
                            mRewardedAd = null
                        }

                        val animation = AnimationUtils.loadAnimation(context, R.anim.adscale)
                        override fun onAdLoaded(rewardedAd: RewardedAd) {
                            Toast.makeText(requireContext(), "Ad is loaded, click the button to view", Toast.LENGTH_SHORT).show()
                            load.text = "Success!! View Ad"
                            load.startAnimation(animation)
                            progressBar.visibility = View.GONE
                            mRewardedAd = rewardedAd
                            mIsLoading = false
                        }
                    }
            )
        }
    }
    private fun getdata1(adspace: CardView, adimage: ImageView, message: TextView){
        var rf = Retrofit.Builder()
                .baseUrl(RetrofitInterface1.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        var API = rf.create(RetrofitInterface1::class.java)
        var call =API.post
        call?.enqueue(object : Callback<AppContent?> {
            override fun onResponse(
                call: Call<AppContent?>,
                response: Response<AppContent?>
            ) {
                var appContent: AppContent? = response.body() as AppContent
                var adslist = appContent?.ads

                if (adslist!![0].visible != false) {
                    adspace.visibility = View.VISIBLE
                    adcheck = true
                    if (adslist!![0].SmallImagelink!!.CheckEmpty() != true) {
                        Glide.with(requireContext()).load(adslist!![0].SmallImagelink).centerCrop()
                            .into(adimage)
                    } else {
                        adimage.visibility = View.GONE
                        adspace.visibility = View.VISIBLE
                    }
                    message.text = adslist!![0].message
                    val animation = AnimationUtils.loadAnimation(context, R.anim.adscale)
                    adspace.startAnimation(animation)
                    adspace.setOnClickListener {
                        adspace.clicking()
                        if (adslist!![0].Link != "") {
                            val uriUri = Uri.parse(adslist!![0].Link)
                            val launchBrowser = Intent(Intent.ACTION_VIEW, uriUri)
                            startActivity(launchBrowser)
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "${adslist!![0].Description}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
                var touse = AdToJsonString(appContent!!)
                Preferencestuff(requireContext()).setLocalAds(touse)
                //  Toast.makeText(requireContext(), "$appContent", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<AppContent?>, t: Throwable) {
                adspace.visibility = View.GONE
                var touse = Preferencestuff(requireContext()).getLocalAds()
                if (touse != ""){
                    var appContent = generateAds(requireContext(), touse!!)
                    var adslist = appContent?.ads

                    if (adslist!![0].visible != false) {
                        adspace.visibility = View.VISIBLE
                        adcheck = true
                        if (adslist!![0].SmallImagelink!!.CheckEmpty() != true) {
                            Glide.with(requireContext()).load(adslist!![0].SmallImagelink).centerCrop()
                                .into(adimage)
                        } else {
                            adimage.visibility = View.GONE
                            adspace.visibility = View.VISIBLE
                        }
                        message.text = adslist!![0].message
                        val animation = AnimationUtils.loadAnimation(context, R.anim.adscale)
                        adspace.startAnimation(animation)
                        adspace.setOnClickListener {
                            adspace.clicking()
                            if (adslist!![0].Link != "") {
                                val uriUri = Uri.parse(adslist!![0].Link)
                                val launchBrowser = Intent(Intent.ACTION_VIEW, uriUri)
                                startActivity(launchBrowser)
                            } else {
                                Toast.makeText(
                                    requireContext(),
                                    "${adslist!![0].Description}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                    //Toast.makeText(requireContext(), "local storage", Toast.LENGTH_SHORT).show()
                }
//                if (response.isSuccessful) {
//
//                } else {
//                    try {
//                        var jObjError = JSONObject(response.errorBody().toString())
//                        Toast.makeText(requireContext(), jObjError.getJSONObject("error").getString("message"), Toast.LENGTH_LONG).show()
//                    } catch (e: Exception) {
//                        Toast.makeText(requireContext(), "${e.message}", Toast.LENGTH_LONG).show()
//                    }
//                }
            }
        })
    }
}