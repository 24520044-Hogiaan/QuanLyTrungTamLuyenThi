# GUI Design Playbook (Swing + FlatLaf)

This playbook defines a reusable blueprint for building modern, bright, premium-looking Swing desktop apps.

## 1) Design Goals
- Clean and premium visual hierarchy.
- 3-layer shell: `Topbar` + `Sidebar` + `Content`.
- Reusable design tokens and component factories.
- No OS-dependent emoji icons.

## 2) Recommended Layout Architecture
- Root: `BorderLayout`
  - `NORTH`: topbar (brand, quick actions/search)
  - `WEST`: sidebar navigation
  - `CENTER`: content area (`CardLayout`)
- Every content page should be wrapped in a page-level `JScrollPane`.
- Every table should also be wrapped in its own `JScrollPane` helper.

## 3) Theme Tokenization
Create a dedicated theme class (`UiTheme`) for:
- Color tokens (`APP_BG`, `CARD_BG`, `PRIMARY`, `TEXT`, ...)
- Font tokens (`TITLE_L`, `TITLE_M`, `BODY`, `CAPTION`)
- Future extensibility for spacing/radius tokens.

## 4) Component Factory Pattern
Create a `UiComponents` class for reusable factories:
- `primaryButton(text)`
- `ghostButton(text)`
- `divider(title)`
- future reusable form rows, chips, cards.

## 5) Sidebar Navigation Rules
- Use grouped sections with visual dividers.
- Use a real selected state, not just bold text:
  - left marker border (or right marker)
  - selected background + bold text
  - hover state distinct from selected state
- Keep vertical spacing compact and consistent.

> The left border indicator pattern is commonly called an **Active Rail** (or **Selection Rail**).

## 6) Forms: Spacing and Readability
- Avoid wide multi-column rows where labels and fields drift apart.
- Prefer `GridBagLayout` or compact `GridLayout(3x2)/(4x2)` for label-field pairs.
- Keep submit actions in a dedicated row, right aligned.

## 7) Card & Depth System
- Cards should have:
  - subtle border
  - soft shadow
  - rounded corners
- Optional micro-animation on hover:
  - shadow intensity transitions
  - lightweight (16ms timer, simple interpolation)

## 7.1) Corner Radius Guidelines
- Keep corner radius consistent by component class:
  - inputs: small/medium radius
  - cards: medium/large radius
  - chips/buttons: medium radius
- Avoid mixing too many radius scales in a single screen.
- Use theme tokens or UI defaults (`Button.arc`, `Component.arc`, `TextComponent.arc`) to enforce consistency.

## 8) Table Styling
- Non-editable model by default.
- Consistent row height (>= 30).
- Soft header background.
- Horizontal grid on, vertical grid off.
- Clear selected-row state.

## 9) Scrollbar & Scrolling Behavior
- Standardize scroll through helper methods.
- Set unit increments for smoother scroll.
- Configure FlatLaf scrollbar tokens:
  - `ScrollBar.width`
  - `ScrollBar.trackArc`
  - `ScrollBar.thumbArc`
  - `ScrollBar.track`
  - `ScrollBar.thumb`

## 10) KPI Formatting Rules
- Do NOT format all KPI values as currency.
- Numeric inventory KPIs should use count units (`books`, `items`, ...).
- Revenue KPIs should use money format (`VND`, etc.).

## 11) i18n / Encoding Rules
- Store all source files in UTF-8.
- Use fully accented Vietnamese (or target locale language) in labels/messages/data.
- Keep business validation messages user-friendly and localized.

## 12) Icon Strategy
- Avoid emoji as primary icon source.
- Use internal Java2D icons (`AppIcons`) or packaged assets (PNG/SVG).
- Centralize icon creation so updates are global and safe.

## 13) Common Mistakes to Avoid
1. **Over-wide forms** causing label/field visual disconnect.
2. **Selected nav ambiguity** (bold-only without marker/background).
3. **Using one formatter for all KPI types** (e.g., adding VND to non-money metrics).
4. **Inconsistent scroll behavior** across pages/tables.
5. **Hardcoded style values everywhere** (no tokenization).
6. **OS-dependent icon rendering** (emoji fallback issues).
7. **Excessive custom painting** without testing repaint/performance.
8. **No page scroll wrapper**, causing clipping on smaller screens.

## 14) Delivery Checklist
- [ ] Compiles cleanly
- [ ] Sidebar selected state is unambiguous
- [ ] Forms are compact and aligned
- [ ] KPI units are semantically correct
- [ ] Scroll works on all major content areas
- [ ] Icons render consistently across machines
- [ ] Localized text is correct and readable

> For screen-by-screen release QA, use: `UI_SCREEN_SNAPSHOT_CHECKLIST.md`.

## 15) Reusable Prompt Template
"Build a Swing + FlatLaf desktop app using this GUI Playbook: Topbar-Sidebar-CardLayout shell, tokenized theme (`UiTheme`), component factory (`UiComponents`), compact form layout, clear selected navigation marker, reusable scroll wrappers, internal non-emoji icons, KPI unit-aware formatting, and UTF-8 localized text."

## 16) Domain-Agnostic Mapping (Use for Any Management App)
When applying this playbook to a new domain, map your app to these generic modules:

- **Master Data** tab(s): core entities (e.g., Products, Employees, Assets, Courses, Patients)
- **Transactions** tab(s): business actions (e.g., Sell, Rent, Check-in/out, Approvals)
- **Parties** tab(s): customers/users/stakeholders
- **History/Logs** tab(s): audit trail and operational timeline
- **Dashboard** tab: KPIs and quick health indicators

### KPI Mapping Rules
- Count KPIs: items/records/active cases/users/tasks
- Monetary KPIs: revenue/cost/profit (currency formatted)
- Time KPIs: processing time/SLA/lead time

### Form Mapping Rules
- Keep label-field pairs tight and readable.
- Use 1-column or 2-column form rows depending on complexity.
- Put primary action buttons in a dedicated action row.

### Navigation Mapping Rules
- Group menu by business function, not by technical layer.
- Keep selected-state explicit (marker + background + typography).

## 17) Extra Pitfalls to Avoid in Management Apps
9. **Mixed semantic units** (e.g., showing currency for count metrics).
10. **Overloaded dashboard** with too many cards and no prioritization.
11. **Unclear action hierarchy** (primary/secondary buttons looking identical).
12. **No empty-state UX** for tables when there is no data.
13. **No validation feedback context** near user input fields.
14. **Layout drift at 125%/150% scaling** on Windows high-DPI screens.
15. **Inconsistent corner radii** between cards, inputs, and buttons.
16. **Crowded sidebar headers/dividers** with uneven vertical rhythm.
