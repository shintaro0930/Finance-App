package jp.co.jri.internship.fintech_sample1.login;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import jp.co.jri.internship.fintech_sample1.CsvReader;
import jp.co.jri.internship.fintech_sample1.UserData;

public class LoginDataSource extends AppCompatActivity {

    public Result<LoggedInUser> login(String userId, String password, Context context) {


        try {
            //csvファイルの読み込み
            CsvReader parser = new CsvReader();
            parser.readerUserDataBase(context);

            //csvファイルの1行目からUserIDやパスワードが合致するかを確認
            for (UserData fdata : parser.userObjects) {
                if (userId.equals(fdata.getUserId()) && password.equals(fdata.getPassword())) {
                    LoggedInUser user = new LoggedInUser(userId, fdata.getDisplayName());
                    return new Result.Success<>(user);
                }
            }

            /* 仮ログイン機能実装として、fakeUserを作成して一律ログイン成功するよう制御 */
            //LoggedInUser user = new LoggedInUser(userId, "日本総研");

            return new Result.Error(new IOException("IDかパスワードが違います"));
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e)); //エラー処理：認証結果失敗としてリターン
        }

    }
}
