package tech.jmcs.floortech.scheduling.app.settings;

public enum SettingsName {
    JOB_FILES_SCHEDULING_PATH ("job_files_scheduling_path"),
    JOB_FOLDERS_DETAILING_PATH ("job_folders_detailing_path"),

    BUILT_IN_TRUSS_EXTRACTOR_ENABLED ("built_in_truss_extractor_enabled"),
    BUILT_IN_TRUSS_FILE_NAME_PATTERN ("built_in_truss_file_name"),
    BUILT_IN_TRUSS_FILE_EXT ("built_in_truss_file_extension"),
    TRUSS_SCHEDULE_SECTION_NAME ("truss_schedule_section_name"),

    BUILT_IN_BEAM_EXTRACTOR_ENABLED ("built_in_beam_extractor_enabled"),
    BUILT_IN_BEAM_FILE_NAME_PATTERN ("built_in_beam_file_name"),
    BUILT_IN_BEAM_FILE_EXT ("built_in_beam_file_extension"),
    BEAM_SCHEDULE_SECTION_NAME ("beam_schedule_section_name"),

    BUILT_IN_SHEET_EXTRACTOR_ENABLED ("built_in_sheet_extractor_enabled"),
    BUILT_IN_SHEET_FILE_NAME_PATTERN ("built_in_sheet_file_name"),
    BUILT_IN_SHEET_FILE_EXT ("built_in_sheet_file_extension"),
    SHEET_SCHEDULE_SECTION_NAME ("sheet_schedule_section_name"),

    BUILT_IN_SLAB_EXTRACTOR_ENABLED ("built_in_slab_extractor_enabled"),
    BUILT_IN_SLAB_FILE_NAME_PATTERN ("built_in_slab_file_name"),
    BUILT_IN_SLAB_FILE_EXT ("built_in_slab_file_extension"),
    SLAB_SCHEDULE_SECTION_NAME ("slab_schedule_section_name"),

    EXCEL_SCHEDULE_SECTION_NAMES ("excel_schedule_section_names"),
    EXCEL_SCHEDULE_SHEET_NAME ("excel_schedule_sheet_name"),

    EXCEL_SCHEDULE_DATA_NAME_COL ("excel_schedule_data_name_col"),
    EXCEL_SCHEDULE_DATA_VALUE_COL ("excel_schedule_data_value_col"),
    EXCEL_SCHEDULE_DATA_UNIT_COL ("excel_schedule_data_unit_col"),
    EXCEL_SCHEDULE_DATA_RATE_COL ("excel_schedule_data_rate_col"),
    EXCEL_SCHEDULE_DATA_TOTAL_COL ("excel_schedule_data_total_col"),
    EXCEL_SCHEDULE_DATA_COMMENT_COL ("excel_schedule_data_comment_col"),

    SCHEDULE_ENTRY_CW260_TRUSS ("schedule_entry_cw260_truss"),
    SCHEDULE_ENTRY_CW346_TRUSS ("schedule_entry_cw346_truss"),
    SCHEDULE_ENTRY_HJ200_TRUSS ("schedule_entry_hj200_truss"),
    SCHEDULE_ENTRY_HJ300_TRUSS ("schedule_entry_hj300_truss"),
    SCHEDULE_ENTRY_STANDARD_EC_CW260 ("schedule_entry_standard_endcaps_cw260"),
    SCHEDULE_ENTRY_STANDARD_EC_CW346 ("schedule_entry_standard_endcaps_cw346"),
    SCHEDULE_ENTRY_STANDARD_EC_HJ200 ("schedule_entry_standard_endcaps_hj200"),
    SCHEDULE_ENTRY_STANDARD_EC_HJ300 ("schedule_entry_standard_endcaps_hj300"),
    SCHEDULE_ENTRY_CONNECTION_EC_CW260 ("schedule_entry_connection_endcaps_cw260"),
    SCHEDULE_ENTRY_CONNECTION_EC_CW346 ("schedule_entry_connection_endcaps_cw346"),
    SCHEDULE_ENTRY_CONNECTION_EC_HJ200 ("schedule_entry_connection_endcaps_hj200"),
    SCHEDULE_ENTRY_CONNECTION_EC_HJ300 ("schedule_entry_connection_endcaps_hj300"),
    SCHEDULE_ENTRY_TRUSS_AIRCON_PENO_CW260 ("schedule_entry_truss_air_con_peno_cw260"),
    SCHEDULE_ENTRY_TRUSS_AIRCON_PENO_CW346 ("schedule_entry_truss_air_con_peno_cw346"),
    SCHEDULE_ENTRY_TRUSS_AIRCON_PENO_HJ300 ("schedule_entry_truss_air_con_peno_hj300"),

    SCHEDULE_ENTRY_STEEL_BLACK_KEYWORD ("schedule_entry_steel_black_keyword"),
    SCHEDULE_ENTRY_STEEL_GALVANISED_KEYWORD ("schedule_entry_steel_galvanised_keyword"),
    SCHEDULE_ENTRY_STEEL_DIMET_KEYWORD ("schedule_entry_steel_dimet_keyword"),
    SCHEDULE_ENTRY_STEEL_EPOXY_KEYWORD ("schedule_entry_steel_epoxy_keyword"),
    SCHEDULE_ENTRY_STEEL_DURAGAL_KEYWORD ("schedule_entry_steel_duragal_keyword"),

    SCHEDULE_ENTRY_SLAB_INTERNAL ("schedule_entry_slab_internal"),
    SCHEDULE_ENTRY_SLAB_2C_RHS ("schedule_entry_slab_2c_rhs"),
    SCHEDULE_ENTRY_SLAB_3C_RHS ("schedule_entry_slab_3c_rhs"),
    SCHEDULE_ENTRY_SLAB_2C_INSITU ("schedule_entry_slab_2c_insitu"),
    SCHEDULE_ENTRY_SLAB_3C_INSITU ("schedule_entry_slab_3c_insitu"),
    SCHEDULE_ENTRY_SLAB_4C_INSITU ("schedule_entry_slab_4c_insitu"),
    SCHEDULE_ENTRY_SLAB_THICK_ANGLE ("schedule_entry_slab_thick_angle"),
    SCHEDULE_ENTRY_SLAB_THIN_ANGLE ("schedule_entry_slab_thin_angle");

    private final String settingsFilePropertyName;

    private SettingsName(String settingsFilePropertyName) {
        this.settingsFilePropertyName = settingsFilePropertyName;
    }

    public String getSettingsFilePropertyName() {
        return settingsFilePropertyName;
    }
}
