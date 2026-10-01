# Android Project 5 - FitApp

Submitted by: **Jaden Clarke**

**FitApp** is a health metrics app that allows users to persistently track daily health metrics including Calories, Water intake, Sleep hours, Exercise duration, and Weight with optional photo attachments.

Time spent: **5** hours spent in total

## Required Features

The following **required** functionality is completed:

- [x] **At least one health metric is tracked (based on user input)**
  - Chosen metric(s): `Calories (kcal)`, `Water (oz)`, `Sleep (hrs)`, `Exercise (mins)`, `Weight (lbs)`
- [x] **There is a "create entry" UI that prompts users to make their daily entry**
  - Prompts users for metric selection, positive numerical input validation, notes, date/timestamp, and optional daily photo attachment.
- [x] **New entries are saved in a database and then updated in the RecyclerView**
  - Managed via SQLite Database (`HealthDatabaseHelper`) and displayed in Jetpack Compose's `LazyColumn` feed with color-coded metric badges and formatted dates.
- [x] **On application restart, previously entered entries are preserved (i.e., are *persistent*)**
  - Fully persistent local SQLite database storage across application restarts.
 
The following **optional** features are implemented:

- [x] **Create a UI for tracking averages and trends in metrics**
  - Analytics tab displaying 7-day average sleep, 7-day average daily calories, total exercise minutes, total logged entries, and goal progress bars.
- [x] **Improve and customize the user interface through styling and coloring**
  - Modern Material 3 design system with custom color palettes for each metric type and elevated cards.
- [x] **Implement orientation responsivity**
  - Adaptive layout powered by `NavigationSuiteScaffold` converting navigation bar (portrait) into navigation rail (landscape/tablet).
- [x] **Add a daily photo feature**
  - Image picker allowing users to attach photos to daily entries with thumbnail previews and removal options.

The following **additional** features are implemented:

- Filter chips to view entries by specific health metric (All, Calories, Water, Sleep, Exercise, Weight).
- Real-time search bar to filter entries by notes or value.
- Delete entry action on each log card.
- Automatic initial sample data populator for quick testing and demoing.

## License

    Copyright 2025 Jaden Clarke

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
