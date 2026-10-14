# Android Project 6 - FitApp

Submitted by: **Jaden Clarke**

**FitApp** is a health metrics app that allows users to track daily health metrics including Calories, Water intake, Sleep hours, Exercise duration, and Weight with optional photo attachments, analytics, and persistent local storage.

Time spent: **6** hours spent in total

## Required Features

The following **required** functionality is completed:

- [x] **Use at least 2 Fragments**
  - Uses `DashboardFragment`, `EntriesListFragment`, and `TrendsFragment` to modularize screens.
- [x] **Create a new dashboard fragment where users can see a summary of their entered data**
  - `DashboardFragment` displays today's metrics vs daily goals, 7-day average calories/sleep, and recent activity cards.
- [x] **Use one of the Navigation UI Views (BottomNavigation, Drawer Layout, Top Bar) to move between the fragments**
  - Uses `NavigationSuiteScaffold` (Bottom Navigation Bar / Navigation Rail) to transition dynamically between the fragments via `FragmentTransaction`.

The following **optional** features are implemented:

- [x] **Add a more advanced UI (e.g: Graphing) for tracking trends in metrics**
  - `TrendsFragment` displays 7-day metric analytics, sleep/calorie averages, goal progress bars, and historical logging summaries.
- [x] **Implement daily notifications to prompt users to fill in their data**
  - `NotificationHelper` and `DailyReminderReceiver` schedule daily reminder notifications prompting users to log their metrics.

The following **additional** features are implemented:

- Filter chips to view entries by specific health metric (All, Calories, Water, Sleep, Exercise, Weight).
- Real-time search bar to filter entries by notes or value.
- Delete entry action on each log card.
- Automatic initial sample data populator for quick testing and demoing.
- Persistent SQLite local database storage across app restarts.
- Image picker to attach daily photos to health log entries.

## Video Walkthrough

Here's a walkthrough of implemented user stories:

<img width="392" height="859" alt="FitApp" src="https://github.com/user-attachments/assets/a61815cb-fc5f-4e7e-ba21-f041b3f65637" />

<!-- Recommended tools:
[Kap](https://getkap.co/) for macOS
[ScreenToGif](https://www.screentogif.com/) for Windows
[peek](https://github.com/phw/peek) for Linux. -->

## Notes

Navigating between Jetpack Compose screens inside standard AndroidX Fragments using `FragmentTransaction` while preserving shared state in `HealthViewModel`.

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
