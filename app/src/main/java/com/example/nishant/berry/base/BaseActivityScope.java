package com.example.nishant.berry.base;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * {@link BaseActivity} scope for dagger
 */
@Scope
@Retention(RetentionPolicy.CLASS)
@interface BaseActivityScope {
}
