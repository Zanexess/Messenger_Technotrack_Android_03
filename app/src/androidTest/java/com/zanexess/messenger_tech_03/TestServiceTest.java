/**
 * Copyrigh Mail.ru Games (c) 2015
 * Created by y.bereza.
 */
package com.zanexess.messenger_tech_03;

import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ServiceTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.google.gson.Gson;
import com.zanexess.messenger_tech_03.Messages.ChannelListData;
import com.zanexess.messenger_tech_03.Messages.LoginData;
import com.zanexess.messenger_tech_03.Messages.Message;
import com.zanexess.messenger_tech_03.Objects.Channel;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@RunWith(AndroidJUnit4.class)
public class TestServiceTest {

	@ClassRule
	public static ServiceTestRule mServiceRule = new ServiceTestRule();
	private static IService messengerService;

	class Callback extends ICallback.Stub {

		@Override
		public void onNewMessage(String data) throws RemoteException {

		}

		@Override
		public void sendToUI(String result) throws RemoteException {

		}

		@Override
		public void onNewUser(String nick, String status) throws RemoteException {

		}
	}

	@BeforeClass
	public static void init() throws TimeoutException {
		Intent startIntent = new Intent(InstrumentationRegistry.getTargetContext(), MessageSocketService.class);
		IBinder binder = mServiceRule.bindService(startIntent);
		messengerService = IService.Stub.asInterface(binder);
	}

	@Test
	public void testAuthCorrect() throws RemoteException, InterruptedException {
		CountDownLatch latch = new CountDownLatch(1);
		Callback callback = new Callback();
		messengerService.bindActivity(callback);
		latch.await(3, TimeUnit.SECONDS);

		//Такой пользователь на сервере есть
		Message message = new Message("auth", new LoginData("111", "111"));
		String msg = new Gson().toJson(message);

		messengerService.sendMessage(msg);
		latch.await(1, TimeUnit.SECONDS);

		String result = messengerService.getCid();

		Assert.assertEquals(result, "111");
	}

	@Test
	public void testAuthIncorrect() throws InterruptedException, RemoteException {
		CountDownLatch latch = new CountDownLatch(1);
		Callback callback = new Callback();
		messengerService.bindActivity(callback);
		latch.await(3, TimeUnit.SECONDS);

		//Такого пользователь нет на сервере
		Message message = new Message("auth", new LoginData("00000000", "00000000"));
		String msg = new Gson().toJson(message);

		messengerService.sendMessage(msg);
		latch.await(1, TimeUnit.SECONDS);

		String result = messengerService.getCid();

		Assert.assertNotEquals(result, null);
	}

	@Test
	public void testChannelList() throws RemoteException, InterruptedException {
		CountDownLatch latch = new CountDownLatch(1);
		Callback callback = new Callback();
		messengerService.bindActivity(callback);
		latch.await(3, TimeUnit.SECONDS);

		//Корректные данные сессии
		Message message = new Message("channellist", new ChannelListData("MY_LOGIN1", "a54b5e9a5b0a9ca09a769e3ab294b698"));
		String msg = new Gson().toJson(message);

		messengerService.sendMessage(msg);
		latch.await(1, TimeUnit.SECONDS);

		List<Channel> result = messengerService.getChannels();

		Assert.assertNotEquals(result, null);
		// Не факт, но допустим
		int res = result.size();
		Assert.assertNotEquals(res, 0);
	}

}
