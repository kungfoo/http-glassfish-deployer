# Deploy to glassfish using HTTP

## Build status

<a href="https://travis-ci.org/kungfoo/http-glassfish-deployer"><img src="https://travis-ci.org/kungfoo/http-glassfish-deployer.svg?branch=master"/></a>

## What

This is (soon) a gradle plugin uses the glassfish admin console REST-like API to deploy apps.

## Why

Because cargo is not cutting it for us and it's very heavy-weight.
Plus it seems that the plugin is abandonware.

## Why not curl?

Curl commands are awesome, don't get me wrong. This is a gradle plugin however and it uses okhttp to do requests.

