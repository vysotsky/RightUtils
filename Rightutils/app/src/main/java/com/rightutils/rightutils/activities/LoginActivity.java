package com.rightutils.rightutils.activities;

/**
 * Created by Anton Maniskevich on 8/18/14.
 */
public interface LoginActivity {

	void sendRequest();

	<T> void doStart(T element);
}
