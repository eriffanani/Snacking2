# Snacking
Styling your snackbar easily and quickly. This library can be used in both Java and Kotlin.

## Installation
#### repositories
```gradle
maven { url 'https://jitpack.io' }
```

#### dependencies
```gradle
implementation 'com.github.eriffanani:Snacking2:1.1.0'
```

## How To Use
#### Xml Layout
```xml
<CoordinatorLayout
    android:id="@+id/parentView"/>
    <ChildView/>
    <ExtendedFloatingActionButton
        android:id="@+id/fab"
        android:layout_gravity="bottom|end"/>
</CoordinatorLayout>
```
#### Activity / Fragment
```kotlin
val parentView: CoordinatorLayout = findViewById(R.id.parentView)
val fab: ExtendedFloatingActionButton = findViewById(R.id.fab)
```

#### Basic
```kotlin
Snacking.Builder(parentView, "Hello! this is basic message")
    .build().show()
```
<img width="350px" src="https://user-images.githubusercontent.com/26743731/197445533-a7fa6556-8a60-4877-8ac8-ab9aa78bae3c.gif"/>

#### With Icon
```kotlin
.icon(R.drawable.ic_info, R.color.teal_200)
```
<img width="350px" src="https://user-images.githubusercontent.com/26743731/197445684-c1bb4542-ea65-4482-8953-bcb8c496a83b.gif"/>

#### With Action
```kotlin
.action("Dismiss", R.color.teal_200) { snackBar ->
    // TODO ACTION
}
```
<img width="350px" src="https://user-images.githubusercontent.com/26743731/197447216-ac200a8f-02dc-4e85-88e3-2de8963c7f1b.gif"/>

#### Use Margin
```kotlin
.useMargin(true)
```
<img width="350px" src="https://user-images.githubusercontent.com/26743731/197447307-287f34d4-db65-48f6-8b7c-35bb9968736c.gif"/>

#### Corner Radius
```kotlin
.cornerRadius(R.dimen.snack_bar_corner_radius)
```
<img width="350px" src="https://user-images.githubusercontent.com/26743731/197447645-be81e62c-1b21-4f40-9b73-76d21503f633.gif"/>

#### Corner Radius (Custom)
```kotlin
// Top Left, Top Right, Bottom Left, Bottom Right
.cornerRadius(R.dimen.snack_bar_corner_radius, 0, 0, R.dimen.snack_bar_corner_radius)
```
<img width="350px" src="https://user-images.githubusercontent.com/26743731/197447696-4d8ab187-1040-4f63-ba4f-52cda192a953.gif"/>

#### Border
```kotlin
.border(R.dimen.snack_bar_border_size, R.color.teal_700)
```
<img width="350px" src="https://user-images.githubusercontent.com/26743731/197447743-c01701cd-4a0e-4d89-9817-81917cbcf557.gif"/>

#### Background Color
```kotlin
.backgroundColor(R.color.purple_200)
```
<img width="350px" src="https://user-images.githubusercontent.com/26743731/197447796-d79237dd-a349-496d-8cad-5321b2d0d201.gif"/>

#### Background Image
```kotlin
.background(R.mipmap.img_gradient)
```
<img width="350px" src="https://user-images.githubusercontent.com/26743731/197447888-a3db670c-ca3e-43d5-8043-1d04ef5e125d.gif"/>

#### Text Color
```kotlin
.textColor("#ffca28") / R.color.colorYellow
```
<img width="350px" src="https://user-images.githubusercontent.com/26743731/197447934-a163c00d-e664-4361-9d96-ca81b000dc65.gif"/>

#### Font
```kotlin
.fontFamily(R.font.montserrat)
```
<img width="350px" src="https://user-images.githubusercontent.com/26743731/197447985-b3d6ab9c-1f82-4073-af5f-8633a7bddc58.gif"/>

#### Anchor View
```kotlin
.anchorView(fab)
```
<img width="350px" src="https://user-images.githubusercontent.com/26743731/197448033-47057d6a-b1ae-406a-b59e-4c472a616a29.gif"/>

#### State
```kotlin
Snacking.State(parentView!!, Snacking.State.INFO)
    .message("This message is using state")
    .show()
```
<img width="350px" src="https://user-images.githubusercontent.com/26743731/197448074-2c3ffd51-d248-478f-8276-fd52b2cbf963.gif"/>

#### Top Position
```kotlin
.position(Snacking.TOP)
```
<img width="350px" src="https://user-images.githubusercontent.com/26743731/197448167-a71bd350-4360-49dc-b07c-84bf11933bbc.gif"/>

#### Message max lines
```kotlin
.messageMaxLines(2)
```
<img width="350px" src="https://user-images.githubusercontent.com/26743731/197448265-6eb87908-452a-42f8-b41a-47de34fcd29d.gif"/>

#### Support for landscape
```kotlin
.landscapeStyle(Snacking.CENTER)
```
<img width="350px" src="https://user-images.githubusercontent.com/26743731/197448306-88601dc1-cc02-4a27-b9f2-d07c5b4f61d7.gif"/>

### Licence
```license
Copyright 2022 Mukhammad Erif Fanani

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
