package com.qromarck.reciperu.Utilities;

import android.content.Context;

public interface LogoutDialogInterface {
    void showLoadingIndicator();
    void hideLoadingIndicator();
    void logout();
    Context getApplicationContext();
}
