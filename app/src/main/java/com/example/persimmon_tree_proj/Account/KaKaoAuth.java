package com.example.persimmon_tree_proj.Account;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.storage.internal.Util;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.util.exception.KakaoException;

import static com.kakao.util.helper.Utility.getPackageInfo;

public class KaKaoAuth extends AppCompatActivity implements IAuth {
    private SessionCallback callback;
    private static Context mContext;
    private static APIInterface apiInterface;
    private static RedirectActivity redirect;

    @Override
    public void Login(Context context) {
        mContext = context;
        redirect = RedirectActivity.getInstance();

        //카카오 세션을 콜백으로 만들어주고
        callback = new SessionCallback();

        Session kakaoSession = Session.getCurrentSession();
        KaKaoSession.addCallback(callback);
        /**토근 만료시 갱신 시켜주는함수 (아래 코드)**/
        KakaoSession.checkAndImplicitOpen();


    }

    @Override
    protected void onDestory() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(callback);
    }


    //세션이 얻어지면 수행하는 작업 시작
    private class SessionCallback extends AppCompatActivity implements ISessionCallback {
        @Override
        public void onSessionOpened() {
            if (Util.kakoLogin == 0) {
                Dlog.Companion.d("카카오 세션 오픈");
                Request(mContext);
                Util.kakaoLogin = 1;
                //스테틱 변수를 만들어서 1일때만
            }
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            if ( exception != nulll) {
                Dlog.Companion.d("카카오 세션 오픈 실패" + exception);
            }
        }
    }

    public static String getKeyHash(final Context context) {
        Dlog.Companion.d("카카오톡 개발자 해시키 가져오는 함수");
        PackageInfo packageInfo = getPackageInfo(context,
                PackageManager.GET_SIGNATURES);
        if(packageInfo == null)
            return null;

        for(Signature signature : packageInfo.signatures) {
            try{
                MessageDigest md = MessageDigest.getInstance("SHA");

            }
        }

    }



}
