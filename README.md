<h1>Weather</h1>
Android weather application created in Kotlin. The project was created based on clean architecture.

<h2>Important information</h2>
<ul>
<li>Free API allows to load weather forecast for 5 days at most. To achieve scrollable effect there is additional 8 items with "Buy" title. In production version should be used prime or elite API package.</li>
<li>Free API allows to make up to 50 requests to the server per day. If you will receive message "Something went wrong." (and you have Internet connection), then replace API key in build.gradle file. You can use one of additional keys, that is in build.gradle (core module). If none of them works, create account on https://developer.accuweather.com and generate new one.</li></ul>

<h2>Used technologies and libraries</h2>
<ul>
<li>Kotlin</li>
<li>MVVM</li>
<li>Architecture Components</li>
<li>Retrofit 2</li>
<li>Dagger 2</li>
<li>RxJava 2</li>
<li>SQLite database (with Room)</li>
<li>JUnit</li>
<li>Mockito</li>
<li>Timber</li>
</ul>

<h2>Modules</h2>
The project is divided into modules: <br>
<b>app</b> - main module containing Application and the only Activity.<br>
<b>core</b> - module used in each other. Contains util classes and scheduler provider.<br>
<b>presentation</b> - contains classes used in feature modules. Currently exists only one feature 
module (cityweather), but in future the next ones will be created.<br>
<b>domain</b> - layer connecting presentation layer with model layer. Contains use-cases, dto's (entities) used in business logic.<br>
<b>repository</b> - model layer consists of repositories and data sources (API and local database). Contains mappers responsible for mapping repository models to domain models and vice versa.<br>
<b>cityweather</b> - feature module, which contains views and business logic.
