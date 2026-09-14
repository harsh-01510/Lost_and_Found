# Implementation Plan - Fix Layout Reference & Modernize Adapter

The goal is to fix the issue where the old design is showing instead of the new `item_layout.xml` and to enhance the Adapter to handle the new UI components.

## User Review Required

> [!IMPORTANT]
> - **Layout Swap**: I will change `ItemAdapter.kt` to inflate `R.layout.item_layout` instead of `R.layout.singleitem`.
> - **Badge Styling**: I will add UI logic to `onBindViewHolder` to dynamically set the background and text of `txtType` based on whether the item is "Lost" or "Found".
> - **Delete Button**: I will bind `btnDelete` in the `ViewHolder`. Since you requested not to change the database logic, I will leave the click listener empty or as a placeholder, ensuring the ID remains valid.

## Proposed Changes

### Adapter Fix

#### [MODIFY] [ItemAdapter.kt](file:///C:/Users/sutha/AndroidStudioProjects/LostFound/app/src/main/java/com/example/lostfound/ItemAdapter.kt)
- Update `onCreateViewHolder` to use `R.layout.item_layout`.
- Update `MyViewHolder` to include `val btnDelete = itemView.findViewById<Button>(R.id.btnDelete)`.
- Update `onBindViewHolder`:
    - Set `txtItemName`, `txtDescription`, `txtLocation`, `txtContact` as before.
    - Add logic for `txtType`:
        - If "Lost" (case-insensitive): Set background to red (#E74C3C) and text to "LOST".
        - If "Found" (case-insensitive): Set background to green (#2ECC71) and text to "FOUND".

### Resource Consistency

#### [MODIFY] [colors.xml](file:///C:/Users/sutha/AndroidStudioProjects/LostFound/app/src/main/res/values/colors.xml)
- Add the specific colors used in the layout and adapter (e.g., `colorLost`, `colorFound`) to ensure they are available as theme attributes or resources.

## Verification Plan

### Manual Verification
- Deploy the app and open the "VIEW ITEMS" screen.
- Verify that the `MaterialCardView` design from `item_layout.xml` is now visible.
- Verify that "Lost" items show a Red badge and "Found" items show a Green badge.
- Verify that the item details (Description, Location, Contact) are correctly displayed under their respective labels.
