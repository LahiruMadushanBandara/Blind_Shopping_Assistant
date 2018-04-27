

package com.beacon.shopping.assistant.injection;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by lahiru on 20/12/2017.
 */
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface UserScope {
}